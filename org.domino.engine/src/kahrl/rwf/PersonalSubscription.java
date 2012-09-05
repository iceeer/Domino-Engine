package kahrl.rwf;
import lotus.domino.*;

import java.util.*;

/** Description: 
 *  This class is used to to handle personal subscriptions and generate scheduled newsletters for users based on their
 *  preferences.
 *
 * @author:  Phillip Kahrl
**/

public class PersonalSubscription {

// The view where personal profiles are kept for newsletter subscriptions
public static final String PERSONAL_SUB_VIEW = "rwfPersonalSubscriptions";
// A date time stamp will be added to the following field each time a subscription is sent for the profile
public static final String NEWSLETTER_SENT_TIME_FIELD = "rwfNewsLetterSentTime";
// The following field will contain the time interval in seconds in which to send subscriptions
public static final String NEWSLETTER_INTERVAL_FIELD = "rwfNewsLetterIntervalTime";
public static final String NOTES_NAME_FIELD = "rwfNewsLetterProfileNotesName";
public static final String EMAIL_ADDRESS_FIELD="rwfNewsLetterEMail";
public static final String SUBJECT_FIELD = "rwfNewsLetterSubject";
public static final String HEADER_FIELD = "rwfNewsLetterHeader";
public static final String FOOTER_FIELD = "rwfNewsLetterFooter";
/// field contains a number for the maximum number of links to be included from a database
public static final String MAX_LINKS_FIELD = "rwfNewsLetterMaxLinks";
//// below are members related to the configuration document for newsletters
public static final String CONFIG_VIEW_KEY = "rwfNewsletterConfig";
public static final String DB_FILE_NAMES = "rwfNewsLetterDbFile";
public static final String PROCESSING_LIMIT_FIELD = "rwfNewsLetterProcessLimit";
public static final String USE_MIME_FIELD = "rwfNewsletterUseMIME";

/** The following fields are used to get subject documents (documents whose links are to be included in the newsletter)

**/

// The following field will contain a list of subscriptions, by name , to include in the newsletter
// The subscription names can be subscription or state names, the getSubjectDocs() method will search
// for these names in the document history.
public static final String NEWSLETTER_SUBSCRIPTION_FIELD = "rwfNewsLetterSubscriptionNames";
// The following are form names to use for documents added
public static final String NEWSLETTER_NEWDOC_FORM_NAMES = "rwfNewsLetterNewDocFormNames";
/// The following field contains form names for modified documents.
public static final String NEWSLETTER_MODDOC_FORM_NAMES = "rwfNewsLetterModDocFormNames";
// The following field contains an expression for a full Text Search
public static final String NEWSLETTER_FTQUERY_FIELD = "rwfNewsLetterFTQuery";
///// the following determines if modified docs will be included with the above query
//// modified docs will be included if the field is set to "true".
public static final String NEWSLETTER_QUERY_MODIFIED_FIELD = "rwfNewsLetteryQueryModified";
/// the following list forms that will be excluded from newsletters
public static final String NEWSLETTER_EXCLUDE_FORMS = "rwfNewsLetterExcludeForm";
/// The following field determines if the links in the newsletter will be numbered sequentially
public static final String NEWSLETTER_NUMBER_LINKS_FIELD = "rwfNewsLetterNumberLinks";
/// the following field name will containd view name is used for finding subject docs by key
public static final String NEWSLETTER_VIEW_FIELD = "rwfNewsletterQueryView";
/// the following field name is used to store keys to search the above view in the profile document
public static final String NEWSLETTER_KEY_FIELD = "rwfNewsletterKeys";

String LINK_PREFIX_FIELD = "rwfNewsLetterLinkPrefix";
String LINK_POSTFIX_FIELD = "rwfNewsLetterLinkPostfix";
String SECTION_TITLE_FIELD = "rwfNewsLetterSectionTitle";
/// set the maximum links to default, will be setter later if the profile has an item MAX_LINK_FIELD with a number in it.
int NEWSLETTER_MAX_LINKS = 50;
/// Mail and DBConfiguration objects for sending mail and getting URL links.
Mail mail;
DBConfiguration config;
// The current database
Database db;
// Document that contains the user profile.
Document profile;
// Determines whether links in the newsletters are numbered.
boolean showLinkNumbers;
// The current Session
Session session;
DateTime now;
// The DateTime a newsletter was last sent
DateTime lastSent;
/// The interval between newsletters
private int interval;
/// View from Subscription class that defines subscription events
View subscriptionView;
boolean useMIME;

/**  The personal subscription constructor
* @param d A Document that contains the profile of the person to whom the newsletter will be sent.
*  The profiles should be in the view as named by PERSONAL_SUB_VIEW.  The form used for the user profiles
*  should include the subform rwfPersonalSubscription.
* @param s - The current session, used for creating DateTime objects.
**/
public PersonalSubscription(Document d, Session s, Mail m, DBConfiguration c ) throws RWFException {
	new PersonalSubscription(d, s, m, c, false);
}

public PersonalSubscription(Document d, Session s, Mail m, DBConfiguration c, boolean u) throws RWFException {
	session = s;
	profile = d;
	mail = m;
	config = c;
	useMIME = u;
	
	try{
		db=profile.getParentDatabase();
		now = db.getCreated();
		now.setNow();
		subscriptionView = db.getView(Subscription.SUBSCRIPTION_VIEW_NAME);
		lastSent = null;
		if(profile.hasItem(NEWSLETTER_SENT_TIME_FIELD) && (profile.getItemValue(NEWSLETTER_SENT_TIME_FIELD).size() != 0 ) ){
			Item item = profile.getFirstItem(NEWSLETTER_SENT_TIME_FIELD);
			if(item.getType() == Item.DATETIMES){
				lastSent = (DateTime)item.getValues().elementAt(0);
			}
			else{
				lastSent = session.createDateTime(profile.getItemValueString(NEWSLETTER_SENT_TIME_FIELD));
				}
	}
	interval = getInterval();
	// make sure the last sent time is not too much further back than the interval
	// the last sent time is adjusted so it is never more than 4 days greater than the interval
	int padding = 4;
	if(now.timeDifference(lastSent) > (interval + (padding*3600*24))){
		int intDays = interval/(3600*24);
		lastSent.setNow();
	     lastSent.adjustDay(-(intDays + padding));
		}
		
	}catch(NotesException e){e.printStackTrace(); }	
}

/** Retrieves all of the databases to be queried for NewsLetters contained in a Configuration document.
*  This method lets all the queried databases to be opened only once for the sendAllNewsLetters method, since
* repeatedly opening and querying the same database can create errors.
@param - d , the current Database
@param s , the current Session
@return a Vector of Databases
**/
static Vector getDbs(Database d, Session s) throws RWFException{
	//// get the NewsLetter databases from the configuration document
	Vector tmp=new Vector(0);
	try{
		View configView = d.getView(DBConfiguration.DB_CONFIG_VIEW_NAME);
		if(configView ==null){
			throw new RWFException("Config View " + DBConfiguration.DB_CONFIG_VIEW_NAME + " not found.");
		}
		Document configDoc = configView.getDocumentByKey(CONFIG_VIEW_KEY);  
		if(configDoc == null){
			throw new RWFException("Newsletter configuration document not found.");
		}
		for(Iterator i=configDoc.getItemValue(DB_FILE_NAMES).iterator();i.hasNext(); ){
			Database qDb = s.getDatabase(d.getServer(), (String)i.next());
			if(!qDb.isOpen()){qDb.open();}
			tmp.addElement(qDb);
			}
		}catch(NotesException e){e.printStackTrace(); return tmp;}
	return tmp;
	}
/** Retrieves the maximum number of profiles to be processed contained in a Configuration document.
 * This limit can be used to control the load on agent manager. 
@param - d , the current Database
@param s , the current Session
@return int - The maximum run time of the newsletter agent, in seconds.
**/
private static int getProcessLimit(Database d) throws RWFException{
	int tmp = 1500;
	try{
		View configView = d.getView(DBConfiguration.DB_CONFIG_VIEW_NAME);
		if(configView ==null){
			throw new RWFException("Config View " + DBConfiguration.DB_CONFIG_VIEW_NAME + " not found.");
		}
		Document configDoc = configView.getDocumentByKey(CONFIG_VIEW_KEY);  
		if(configDoc == null){
			throw new RWFException("Newsletter configuration document not found.");
		}
		int tl = configDoc.getItemValueInteger(PROCESSING_LIMIT_FIELD);
		if(tl >0){tmp=tl;}
		}catch(NotesException e){e.printStackTrace(); return tmp;}
	return tmp;
	}
/** Retrieves the whether to use MIME format for newsletters contained in a Configuration document.
@param - d , the current Database
@return  - Use MIME format.
**/
private static boolean useMIME(Database d) throws RWFException{
	boolean tmp = false;
	try{
		View configView = d.getView(DBConfiguration.DB_CONFIG_VIEW_NAME);
		if(configView ==null){
			throw new RWFException("Config View " + DBConfiguration.DB_CONFIG_VIEW_NAME + " not found.");
		}
		Document configDoc = configView.getDocumentByKey(CONFIG_VIEW_KEY);  
		if(configDoc == null){
			throw new RWFException("Newsletter configuration document not found.");
		}
		String fieldVal = configDoc.getItemValueString(USE_MIME_FIELD);
		if("true".equals(fieldVal)){
			tmp=true;}
		}catch(NotesException e){e.printStackTrace(); return tmp;}
	return tmp;
	}

/**
Sends newsletters for all profiles in the current database.
@param, db the current Database,
@param session , the current Session.
@throws RWFException
**/
public static void sendAllNewsLetters(Database db, Session session) throws RWFException{
	try{
		/// get DateTimes for the timer
		DateTime start = session.createDateTime("01/01/2005");
		DateTime currentTime = session.createDateTime("01/01/2005");
		start.setNow();
		currentTime.setNow();
		boolean useMIME = useMIME(db);
		int limit = 1500;
		try{
			limit = getProcessLimit(db);
		}catch(RWFException x){x.printStackTrace(); }
		Vector nldbs = getDbs(db, session);
		Mail mail = new Mail(db);	
		DBConfiguration config = null;
		try{
			config = new DBConfiguration(db);
		}catch(ConfigDocNotFoundException e){e.printStackTrace(); }
		View view = db.getView(PersonalSubscription.PERSONAL_SUB_VIEW);
		Vector profiles = new Vector(0);
		Document doc = view.getFirstDocument();
		int count = 0;
		LOOP:while(doc != null){
			if(count++ > limit){
				break LOOP;
			}
			profiles.addElement(doc.getUniversalID());
			doc=view.getNextDocument(doc);
		}
		/// loop through and send a newsleter for each profile.
		LOOP:for(Iterator it=profiles.iterator(); it.hasNext(); ){	
			String unid = (String)it.next();
			Document psDoc = db.getDocumentByUNID(unid);
			PersonalSubscription ps = new PersonalSubscription(psDoc, session, mail, config, useMIME);
			ps.sendNewsLetter(nldbs);
			ps = null;
			}
		System.gc();
		mail = null;
		start = null;
		currentTime = null;
		nldbs = null;
		profiles = null;
		view = null;
		doc = null;
		}catch(NotesException e){e.printStackTrace(); }
	}

/** Determines the interval between newsletters from the user profile document, in seconds.
@return - The number contained in the field named by member NEWSLETTER_INTERVAL_FIELD in the profile document.
* if no value is found in the field 85400 is returned (approx. 1 day).
**/
private int getInterval(){
	int interval = 24*3600 -1000;
	try{
		Item intervalItem = profile.getFirstItem(NEWSLETTER_INTERVAL_FIELD);
		/// the interval field can be either text or number
		if(intervalItem.getType() == Item.TEXT){
			interval = new Integer(profile.getItemValueString(NEWSLETTER_INTERVAL_FIELD)).intValue();
			}
		if(intervalItem.getType() == Item.NUMBERS){
		interval = profile.getItemValueInteger(NEWSLETTER_INTERVAL_FIELD);
			}
		}catch(NotesException e){
			
			return interval;
			}
		catch(Exception e){return interval;}
	return interval;	
}

/*** determine if a given name is a reader for a given document
@param name - a Notes User Name
@param mDoc - the Document to check for Reader fields
@return - false if mDoc contains Readers field(s) , none of which include the passed name.
***/

public boolean isReader(String name, Document mDoc){
	boolean tmp = true;
	try{
		Vector items = mDoc.getItems();
		Vector readers = new Vector(0);
		for(Iterator it = items.iterator(); it.hasNext(); ){
			Item item = (Item)it.next();
			if(item.getType() == Item.READERS){
				readers.addElement(item.getValues());
			}
		}
		readers = Approver.parseGroups(Approver.parseRoles(Approver.parseVec(readers), db), session);
		if((readers.size() > 0) && !readers.contains(name)){tmp = false; }
	} catch(NotesException e){
			e.printStackTrace(); 
			return tmp;
			}
	return tmp;
	}

/** 
Adds documents to a Vector for a database and query for the current profile.
@param - documents , a Vector of Document containing the current list of Documents whose links are to be included
* in the newsletter.  This is used to ensure that duplicate document links are nto included.
@param queryDb - The database being queried.
@param query A String containing a query to be used for either a Database.Search() or a Database.FTSearch(), the format
* of query is dependent upon the type of search specified by the FTSearch parameter.  
@param getModified a boolean that determines whether Documents modified since the last newsletter was sent that meet the 
* search criteria will be included.  If getModified==false, only new Documents will be included.
@param FTSearch determines whether a Database.Search or a Database.FTSearch will be used.  If FTSearch==true then a Full Text
* search will be run otherwise a Search will be run.
@return - A Vector of Documents that will include all Documents in the documents parameter, plus any addtional documents
* that were returned from the search specified by the query parameter.
**/
Vector getDocsForQuery(Vector documents , Database queryDb, String query, boolean getModified, boolean FTSearch){
if((query == null) || (query == "")){return documents; }
/// don't look for any more documents if we have exceeded the max number of links
if(documents.size() > NEWSLETTER_MAX_LINKS*5){
	return documents; 
}
Log log = null;
try{
	log= new Log(profile.getParentDatabase());
	if((FTSearch) && !queryDb.isFTIndexed()){
	log.println("Database " + queryDb + " is not full-text indexed");}
	DocumentCollection dc = null;
	if(FTSearch){
		dc = queryDb.FTSearch(query, 0);
		}else	
		{
			dc = queryDb.search(query, lastSent);
		}
	//if(dc != null){
		Document doc = dc.getFirstDocument();
		while(doc != null){
			DateTime eventTime = doc.getCreated();
			if(getModified){eventTime = doc.getLastModified(); }
			//System.out.println("eventTime=" + eventTime + " for doc=" + doc);
			//System.out.println("timeDifference=" + eventTime.timeDifference(lastSent));
			if((eventTime != null) && (eventTime.timeDifference(lastSent) > 0 )){
				if(!documents.contains(doc)){
					documents.addElement(doc);
					/// don't look for any more documents if we have exceeded the max number of links
					if(documents.size() > NEWSLETTER_MAX_LINKS*5){
						return documents; 
					}
				}
			}
			doc = dc.getNextDocument(doc);
		//}
		}
	}catch(NotesException e){
		log.println("NotesException: " + e.text);
		log.println("Error with query=" + query);
		log.println("queryDb=" + queryDb);
		log.println("FTSearch=" + FTSearch);
		log.println("Error with query = " + query + " in database " + db + " in user profile " + profile);
		return documents;
		}
	catch(Exception e){
		e.printStackTrace();
		return documents;
		}
return documents;
}
/**
*  Gets docs from a view in the queried database by key.  Returns all docs from mulitple keys
*  passed as a Vector.
* @Return a Vector of documents
* @param documents - A Vector containing the current subject documents
* @param queryDb - Database containing the view
* @param - viewFieldName the name of the field containing the view in the profile document
* @param - keyFieldName the name of the field containing keys to for documents to find in the view.
**/

Vector getDocsByKey(Vector documents, Database queryDb, String viewFieldName, String keyFieldName, boolean getModified){
	try{
		// check to see if the profile doc has view and key names
		if(profile.hasItem(viewFieldName) && (profile.getItemValueString(viewFieldName) != null) 
		 && profile.hasItem(keyFieldName)  && (profile.getItemValueString(keyFieldName) != null )){
		 	/// get the view from the queried database
			View view = queryDb.getView(profile.getItemValueString(viewFieldName));
			/// if the view isn't null
			if(view != null){
				DocumentCollection dc=view.getAllDocumentsByKey("*");
				dc.removeAll(true);
				Document doc;
				/// loop through all the keys and get documents from the view
				LOOP:for(Iterator i=profile.getItemValue(keyFieldName).iterator(); i.hasNext(); ){
					String key = (String)i.next();
					// account for the wild card.
					if(key != null){
						if("*".equals(key)){
							doc = view.getFirstDocument();
							while(doc != null){
						 		try{
						 			dc.addDocument(doc);
						 		}catch(NotesException ex){
						 			/// do nothing, doc is already in the collection
						 		}
						 		doc = view.getNextDocument(doc);
						 	}
						 }  // if "*".equals(key)
						 else{dc = view.getAllDocumentsByKey(key);}
						} // key != null
					} /// LOOP
				    /// now go through all the docs obtained from the views and check the created or modified dates.
				 if(dc != null){
						doc = dc.getFirstDocument();
						while(doc != null){
							DateTime eventTime = doc.getCreated();
							if(getModified){eventTime = doc.getLastModified(); }
							if((eventTime != null) && (eventTime.timeDifference(lastSent) > 0 )){
							if(!documents.contains(doc)){
								documents.addElement(doc);
								/// don't look for any more documents if we have exceeded the max number of links
								if(documents.size() > NEWSLETTER_MAX_LINKS*5){
									return documents; 
									}
								}
							}
						doc = dc.getNextDocument(doc);
						}  /// while doc != null
					}
		
		     	} // if  has view and key names
			} // if (view != null)		
		}catch(NotesException e){e.printStackTrace() ; }
	return documents;
}

/**
*  Creates a Vector of all documents in the database that will be included as URL links when sending the newsletter.
*   This method searches the history of fields of all documents in the database and looks for subscriptions that 
* match the subscription  names  found in the field NEWSLETTER_SUBSCRIPTION_FIELD.  It returns only subscriptions that have occured 
* since the last time a newsletter was sent for the current profile.  
*
@return A Vector containing elements of type Document.
**/
public Vector getSubjectDocs(int i, Vector dbs){
	Vector tmp = new Vector(0);
	try{
		/// loop through all the databases
		Database subDb = (Database)dbs.elementAt(i);
		////  get all new documents with the specified form
		/// first build a query string
		// the field numbering convention starts with 1, not zero.
		i++;
		Vector forms = profile.getItemValue(NEWSLETTER_NEWDOC_FORM_NAMES+i);
		String formQuery = "";
		if(forms.size() > 0){
			formQuery = "@Contains(Form;";
			for(Iterator it = forms.iterator(); it.hasNext(); ){
				String f = (String)it.next();
				formQuery = formQuery + "\"" + f + "\":";
			}
		formQuery = formQuery.substring(0, formQuery.lastIndexOf(":")) + ")";
		}
		/// account for the wild card
		boolean ftsearch = false;
		if("*".equals(profile.getItemValueString(NEWSLETTER_NEWDOC_FORM_NAMES+i) )){
			formQuery = "@All";
			ftsearch = false;
			}
		/// add documents for the query string
		tmp = getDocsForQuery(tmp, subDb, formQuery, false, ftsearch);
		////  get all modified documents with the specified form
		/// build another query string
		ftsearch = false;
		forms = profile.getItemValue(NEWSLETTER_MODDOC_FORM_NAMES+i);
		formQuery = "";
		if(forms.size() > 0){
			formQuery = "@Contains(Form;";
			for(Iterator it = forms.iterator(); it.hasNext(); ){
				String f = (String)it.next();
				formQuery = formQuery + "\"" + f + "\":";
			}
		formQuery = formQuery.substring(0, formQuery.lastIndexOf(":")) + ")";
		}
		// account for the wild card
		if("*".equals(profile.getItemValueString(NEWSLETTER_MODDOC_FORM_NAMES+i) )){
			formQuery = "@All";
			ftsearch = false;
			}
		tmp = getDocsForQuery(tmp, subDb, formQuery, true, ftsearch);
		//// Get the Field Change/state  Subscriptions
		String fcQuery = "";
		//	
		Vector subNames = profile.getItemValue(NEWSLETTER_SUBSCRIPTION_FIELD+i);
		if(subNames.size() > 0){
			fcQuery = "@Contains(" + Approver.RWF_HISTORY_STATUS_FIELD + ";";
			for(Iterator it=subNames.iterator(); it.hasNext(); ){
				String f = (String)it.next();
				fcQuery = fcQuery + "\"" + f + "\":";
				}
				fcQuery = fcQuery.substring(0, fcQuery.lastIndexOf(":")) + ")";
			}
			DocumentCollection fcDc = subDb.search(fcQuery, lastSent);
			Document fcDoc = fcDc.getFirstDocument();
			while(fcDoc != null){
				DateTime eventTime = session.createDateTime("01/01/2004");
				Approver approver = new Approver(fcDoc);
				LOOP:for(Enumeration e=subNames.elements(); e.hasMoreElements(); ){
					String sub = (String)e.nextElement();
					String subTimeString = approver.getTimeForStatus(sub);
					if(subTimeString != null){
						DateTime subTime = session.createDateTime(subTimeString);
						if(subTime.timeDifference(lastSent) > 0){
							eventTime = subTime;					
							break LOOP;
						}
					}
				}
				if((eventTime.timeDifference(lastSent) > 0) && !tmp.contains(fcDoc) ){
					tmp.addElement(fcDoc);
				}
				fcDoc = fcDc.getNextDocument(fcDoc);
			}
	/// now get any new/modified documents that match the ft query
	boolean modFlag = false;
	if("true".equals(profile.getItemValueString(NEWSLETTER_QUERY_MODIFIED_FIELD + i))){
		modFlag = true;
		}
	tmp = getDocsForQuery(tmp, subDb, profile.getItemValueString(NEWSLETTER_FTQUERY_FIELD+i), modFlag, true);
	/// get the docs by the view quesry
	tmp = getDocsByKey(tmp, subDb, NEWSLETTER_VIEW_FIELD + i, NEWSLETTER_KEY_FIELD + i, modFlag);	
	/// now filter for readers
	tmp = filterReaders(tmp);
	
	/// now filter excluded forms
	if(profile.hasItem(NEWSLETTER_EXCLUDE_FORMS + i) && (profile.getItemValueString(NEWSLETTER_EXCLUDE_FORMS + i) != null)){
		tmp = filterExcludeForms(profile.getItemValue(NEWSLETTER_EXCLUDE_FORMS + i), tmp);	
	}	
	} catch(NotesException e){e.printStackTrace(); return tmp;}	
return tmp;
}
/**
* filter out documents with exclude forms.
* @param - formNames , A Vector of Strings containing the names of Forms.
* 
**/
Vector filterExcludeForms(Vector formNames, Vector documents){
	Vector trash = new Vector(0);
	try{
		for(Iterator i=formNames.iterator(); i.hasNext(); ){
				String excludeForm = (String)i.next();
				for(Iterator docE = documents.iterator(); docE.hasNext(); ){
					Document doc = (Document)docE.next();
					if(doc.hasItem("Form") && (doc.getItemValueString("Form") != null) ){
						if(excludeForm.equalsIgnoreCase(doc.getItemValueString("Form"))){
							trash.addElement(doc);
							}
						}
					}
				}
			}catch(NotesException e){e.printStackTrace(); return documents;}
	for(Iterator i=trash.iterator(); i.hasNext() ; ){
		Document doc = (Document)i.next();
		documents.removeElement(doc);
		}
	return documents;
	}

/**
given a Vector containing documents, it removes any documents that have readers fields that do not match the notes name of the current profile
Works with readers fields that containg group names and user roles
**/
private Vector filterReaders(Vector docs){
Vector trash = new Vector(0);
try{	
	for(Iterator i = docs.iterator(); i.hasNext(); ){
		Document doc = (Document)i.next();
		String notesName = profile.getItemValueString(NOTES_NAME_FIELD);
		if(notesName != null){
			if(!isReader(notesName, doc )){
				trash.addElement(doc);
			 	}
			}
		}
} catch (NotesException e){e.printStackTrace(); return docs;}	

for(Iterator i=trash.iterator(); i.hasNext(); ){
			Document doc = (Document)i.next();
			docs.removeElement(doc);
			}	
return docs;
}
/**
* Determines if links in the newsletter will be numbered sequentially based on the value of the field defined by
* NEWSLETTER_NUMBER_LINKS_FIELD in the profile document.
* @Return true in the field determined by NEWSLETTER_NUMBER_LINKS_FIELD contains the value "yes", false otherwise.
**/
boolean numberLinks(){
	boolean tmp = false;
	try{
		if((profile.hasItem(NEWSLETTER_NUMBER_LINKS_FIELD)) && (profile.getItemValueString(NEWSLETTER_NUMBER_LINKS_FIELD) != null) && (profile.getItemValueString(NEWSLETTER_NUMBER_LINKS_FIELD) .equals("yes"))){
			tmp = true;
			}
		}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}

/** 
* Creates and sends a newsletter for the current profile.
* @param dbs - A Vector of all the Databases to be queried fo the newsletters.
**/
public void sendNewsLetter(Vector dbs){
	Log log = null;
	/// get the url of the current server
	try{
		String serverURL = db.getServer();
		if(config != null){
			serverURL = config.getServerURL();
		}
		/// get the max number of links for the profile
		if(profile.hasItem(MAX_LINKS_FIELD)){
			Item maxLinkItem = profile.getFirstItem(MAX_LINKS_FIELD);
			if(maxLinkItem.getType() == Item.TEXT){
				NEWSLETTER_MAX_LINKS= new Integer(profile.getItemValueString(MAX_LINKS_FIELD)).intValue();
			}
			if(maxLinkItem.getType() == Item.NUMBERS){
					NEWSLETTER_MAX_LINKS = profile.getItemValueInteger(MAX_LINKS_FIELD);
			}
		}
		if((lastSent==null) || (now.timeDifference(lastSent) > interval )){
			/// Get a mail object, subject and body for the memo to be sent.
			log = new Log(profile.getParentDatabase());
			String subject = profile.getItemValueString(SUBJECT_FIELD);
			if(subject == null){subject="None";}
			String body = profile.getItemValueString(HEADER_FIELD);
			if(body == null){body = "";}else{body = body + '\n' + '\n';}
			Vector recipients = profile.getItemValue(EMAIL_ADDRESS_FIELD);
		     /// determines if links will have numbers in front of them
			boolean numberLinks = numberLinks(); 
			/// go through each database listed in the profile and find all the documents to include as links in the newsletter
			boolean sendMemo = false;
			DBLOOP:for(int i=0;i<dbs.size();i++){
				int k=i+1;
				Vector subjectDocs = getSubjectDocs(i, dbs);
				if(subjectDocs.size() > 0 ){
					sendMemo = true;
					/// add the section header
					if(profile.hasItem(SECTION_TITLE_FIELD + k) && (profile.getItemValueString(SECTION_TITLE_FIELD + k) != null)){
						body = body + profile.getItemValueString(SECTION_TITLE_FIELD + k) + '\n';
						}
					int limitCount = 1;
					int count = 0;
					LOOP:for(Iterator it=subjectDocs.iterator(); it.hasNext(); ){
						if(limitCount++ > NEWSLETTER_MAX_LINKS){break LOOP; }
						Document doc = (Document)it.next();
						String prefix = "";
						if(profile.hasItem(LINK_PREFIX_FIELD+k) && (profile.getItemValueString(LINK_PREFIX_FIELD+k) != null)){ 
							prefix = Mail.subFieldValues(profile.getItemValueString(LINK_PREFIX_FIELD+k), doc, useMIME);
						}
						
						String postfix = "";
						if(profile.hasItem(LINK_POSTFIX_FIELD+k) && (profile.getItemValueString(LINK_POSTFIX_FIELD+k) != null)){ 
							postfix = Mail.subFieldValues(profile.getItemValueString(LINK_POSTFIX_FIELD+k), doc, useMIME);
						}
						
						if(numberLinks){body = body + ++count + ".  ";}
						
						body = body + prefix + serverURL + "/" + doc.getParentDatabase().getFilePath().replace('\\','/') + "/0/" + doc.getUniversalID() + "?OpenDocument" + postfix + '\n';			
					}	
				} 
			} 
			String footer = profile.getItemValueString(FOOTER_FIELD);
			footer = (footer==null)?"":footer;
			body = body  +  '\n' + profile.getItemValueString(FOOTER_FIELD);
			// make sure the body isn't too big for a field
			if(body.length() >21000){body = body.substring(0,21000);}
			if(sendMemo){
				log.println("Sending newsletter to " + recipients);
				log.println("newsletter body = " + body);
				if(useMIME){
					mail.sendMail(recipients, new Vector(0), new Vector(0), subject, body, useMIME);
				}else{
					mail.sendMail(recipients, subject, body);
				}
			}
			/// update the sent time on the profile and save it.
			profile.replaceItemValue(NEWSLETTER_SENT_TIME_FIELD, now);
			profile.save();
		}
	}catch(NotesException e){e.printStackTrace();}				
	}
}
