package kahrl.rwf;
import lotus.domino.*;
import java.util.*;

/* Description: 
 *  This class deals with notification configuration documents and is responsible for sending emails based for both subscriptions and states.
 *
 * @author:  Phillip Kahrl
*/

public class Notification {
//* Field definitions controls interaction with Notes Forms */
	public static final String NOTIFICATION_NAME_FIELD = "rwfMailConfigKey";
	public static final String MAIL_SUBJECT_FIELD = "rwfMailSubject";
	public static final String MAIL_BODY_FIELD = "rwfMailBody";

	public static final String REPLY_TO_FIELD = "rwfMailReplyTo";	
	
	public static final String RECIPIENT_OPTION_FIELD = "rwfMailSendOption";
	public static final String MAIL_RECIPIENTS_FIELD = "rwfRecipientsNames";
	public static final String RECIPIENT_ROLES_FIELD = "rwfRecipientsUserRoles";
	public static final String MAIL_RECIPIENTS_FIELDNAMES = "rwfRecipientsFieldname";
	public static final String MAIL_SEND_SEPARATE_FIELD = "rwfRecipientsSendSeparate";
	
	public static final String CCRECIPIENT_NAMES_FIELD = "rwfCCRecipientsNames";
	public static final String CCRECIPIENT_ROLES_FIELD = "rwfCCRecipientsUserRoles";
	public static final String MAIL_CCRECIPIENTS_FIELDNAMES = "rwfCCRecipientsFieldname";
	
	public static final String BCCRECIPIENT_NAMES_FIELD = "rwfBCCRecipientsNames";
	public static final String BCCRECIPIENT_ROLES_FIELD = "rwfBCCRecipientsUserRoles";
	public static final String MAIL_BCCRECIPIENTS_FIELDNAMES = "rwfBCCRecipientsFieldname";
		
	public static final String RWF_APPROVER_NAMES_FIELD = Approver.RWF_APPROVER_NAMES_FIELD;
	public static final String RWF_AUTHOR_FIELD = Approver.RWF_AUTHOR_FIELD;
	
	public static final String RWF_NOTIFICATION_TYPE_FIELD = "rwfMailNotificationType";
	public static final String RWF_ESCALATION_HOURS_FIELD = "rwfEscalationHours";
	public static final String ESCALATION_EXPIRE_FIELD = "rwfEscalationExpireField";
	public static final String RWF_ESCALATION_NEW_STATE_FIELD = "rwfEscalationState";
	
	public static final String ATTACH_LINK_FIELD = "rwfAttachLink";	
	public static final String ATTACH_DOCLINK_FIELD = "rwfAttachDocLink";
	public static final String ATTACH_LINK_TRUE = "1";
	public static final String SENDTO_INTERNET_ADDRESS_FIELD = "rwfSendToInternet";

	public static final String ADDRESS_VAL_FIELDS_FIELD="rwfAddressValFields";
	public static final String MSG_IMPORTANCE_FIELD = "rwfMailImportance";
	public static final String MSG_FORMAT_FIELD="rwfMailMsgMsgType";

	/* End of Field definitions */
	/// (Database) holds the address books
	
	private Vector nabs= new Vector(0);
	
	// doc will be the notification definition document
	// a document being acted on in the workflow will be passed as an argument
	private Document doc;
	private Database db;
	
	/** 
	* Constructor for the Notification document.  The constructor creates a notification document based on a notification configuration document.
    	* @param  d  A notification configuration document.
    	**/
	public Notification (Document d){
		doc = d;
		try{
			db = doc.getParentDatabase();
			nabs = db.getParent().getAddressBooks();
			
		}catch(NotesException e){e.printStackTrace();}
	}	
/** 
	*  Gets a list of all recipients for the current notification object.  Recipients names will be put into the SendTo field of the generated notification.
	* @return A Vector of strings containing names or email addresses.
	* @param d  The context document
**/
		public Vector getRecipients(Document d){
		Vector tmp = new Vector(0);
		try{
			// Check to see if the notification document lists approvers, authors, or readers as recipients
			Vector sendOptions = doc.getItemValue(RECIPIENT_OPTION_FIELD);
			if(sendOptions.contains("1")){
				tmp.addElement(d.getItemValue(RWF_APPROVER_NAMES_FIELD));
				tmp.addElement(d.getItemValue(Approver.RWF_DELEGATES_FIELD));
			}
			if(sendOptions.contains("2")){tmp.addElement(Approver.parseRoles(d.getItemValue(RWF_AUTHOR_FIELD), db));}
			//
			// Get the recipients that are listed by name in the Mail Notification document
			tmp.addElement(doc.getItemValue(MAIL_RECIPIENTS_FIELD));
			// Go through and get all the recipients from passed document
			// using the fields listed in the notification document.
			Vector recipientsFromFields = getRecipientFields(d);
			for(Iterator i = recipientsFromFields.iterator();i.hasNext();){
				tmp.addElement((String)i.next());
				}
			// Get the recipients by user role(s)
			for(Iterator roles = doc.getItemValue(RECIPIENT_ROLES_FIELD).iterator(); roles.hasNext();){
				String roleName = (String)roles.next();
				tmp.addElement(Approver.getNamesByRole(roleName, db));
			}	
			tmp = Approver.parseVec(tmp);
			/// remove empties
			while(tmp.contains("")){
				tmp.removeElement("");
				}
			if(sendToInternet()){tmp.addElement(getInternetAddresses(tmp));}
			tmp = Approver.parseVec(tmp);
			}catch(NotesException e){e.printStackTrace();}
		return tmp;
		}
/** 
	*  Determines whether the notification configuration document has the "Send to Internet Address" option checked.
	*  The send to internet option determines whether mail is sent to a the value in the "InternetAddress" field found in the person doc in addition
	*  to their notes mail address.
	* @return A boolean determining whether or not the "Send To Internet" option is checked in the notification configuration document.
	* @see getInternetAddresses()
**/
	private boolean sendToInternet(){
		boolean tmp = false;
		try{
			if(doc.hasItem(SENDTO_INTERNET_ADDRESS_FIELD)){
				String val = doc.getItemValueString(SENDTO_INTERNET_ADDRESS_FIELD);
				if((val != null) && val.equals("1")){
					tmp=true;
					}
				}
			}catch(NotesException e){e.printStackTrace(); return tmp;}
		return tmp;
		}
		
		private Document getStateDocument(){
			Document tmp = null;
			try{
				tmp = db.getDocumentByUNID(doc.getParentDocumentUNID());
				}catch(NotesException e){e.printStackTrace(); return tmp; }
			return tmp;
		}
	/** 
	*  For a list of names, this will return a list of internet address from person docs for all names in the list from the NAB.
	*  The internet address for a person is determined by the value of the field "InternetAddress" from their person doc.
	* @return A Vector of strings consisting of internet address for all names listed in the parameter.
	* @param  A list of names.
**/
	private Vector getInternetAddresses(Vector names){
		Vector tmp = new Vector(0);
		/// return a Vector of internet addresses "InternetAddress" field from the Address books for a vector of names
		try{
			for(Iterator i=nabs.iterator(); i.hasNext(); ){
				Database nab = (Database)i.next();
				if(!nab.isOpen()){nab.open();}
				View users = nab.getView("($Users)");
				for(Enumeration e1=names.elements(); e1.hasMoreElements(); ){
					String name = (String)e1.nextElement();
					Document personDoc = users.getDocumentByKey(name);
					if(personDoc != null){
						String ia = personDoc.getItemValueString("InternetAddress");
						if(ia != null){tmp.addElement(ia);}
						}
					
				} 
				}
		}catch(NotesException e){e.printStackTrace(); return tmp;}
		return tmp;	
	}
/// gets a list of fields in the context document form the notification configuration document for Copyto recipients.
	private Vector getCCRecipientFields(Document d){
			Vector tmp = new Vector(0);
			try{
				Vector fields = doc.getItemValue( MAIL_CCRECIPIENTS_FIELDNAMES);
				for(Iterator i=fields.iterator(); i.hasNext(); ){
					String fieldName = (String)i.next();
					Vector val = d.getItemValue(fieldName);
					tmp.addElement(val);
				}
		}catch(NotesException e){e.printStackTrace();}
		return Approver.parseVec(tmp);
		}
		
/// gets a list of fields in the context document form the notification configuration document for BlindCopyto recipients.
private Vector getBCCRecipientFields(Document d){
			Vector tmp = new Vector(0);
			try{
				Vector fields = doc.getItemValue( MAIL_BCCRECIPIENTS_FIELDNAMES);
				for(Iterator i=fields.iterator(); i.hasNext(); ){
					String fieldName = (String)i.next();
					Vector val = d.getItemValue(fieldName);
					tmp.addElement(val);
				}
		}catch(NotesException e){e.printStackTrace();}
		return Approver.parseVec(tmp);
		}
/// gets a list of fields in the context document form the notification configuration document for SendTo recipients.
private Vector getRecipientFields(Document d){
		Vector tmp = new Vector(0);
			try{
				Vector fields = doc.getItemValue( MAIL_RECIPIENTS_FIELDNAMES);
				for(Iterator i=fields.iterator(); i.hasNext(); ){
					String fieldName = (String)i.next();
					Vector val = d.getItemValue(fieldName);
					tmp.addElement(val);
				}
		}catch(NotesException e){e.printStackTrace();}
		return Approver.parseVec(tmp);
		}
	/** 
	*  Gets a list of all Copy To recipients for the current notification object. CopyTo recipients names will be put into the CopyTo field of the generated notification.
	* @return A Vector of strings containing names or email addresses.
	* @param d  The context document
**/
	public Vector getCCRecipients(Document d){
		Vector tmp = new Vector(0);
		try{
			// Get the recipients that are listed by name in the Mail Notification document
			tmp = doc.getItemValue(CCRECIPIENT_NAMES_FIELD);
			// Go through and get all the recipients from passed document
			// using the fields listed in the notification document.
			Vector recipientsFromFields = getCCRecipientFields(d);
			for(Iterator i = recipientsFromFields.iterator(); i.hasNext();){
				tmp.addElement((String)i.next());
				}
			// Get the recipients by user role(s)
			for(Iterator roles = doc.getItemValue(CCRECIPIENT_ROLES_FIELD).iterator(); roles.hasNext();){
				String roleName = (String)roles.next();
				tmp.addElement(Approver.getNamesByRole(roleName, db));
			}	
			tmp = Approver.parseVec(tmp);
			if(sendToInternet()){tmp.addElement(getInternetAddresses(tmp));}
			tmp = Approver.parseVec(tmp);
			}catch(NotesException e){e.printStackTrace();}
		return tmp;
		}			
/** 
	*  Gets a list of all Blind Copy To recipients for the current notification object. Blind Copy To recipients names will be put into the BlindCopyTo field of the generated notification.
	* @return A Vector of strings containing names or email addresses.
	* @param d  The context document
**/
public Vector getBCCRecipients(Document d){
		Vector tmp = new Vector(0);
		try{
			// Get the recipients that are listed by name in the Mail Notification document
			tmp = doc.getItemValue(BCCRECIPIENT_NAMES_FIELD);
			// Go through and get all the recipients from passed document
			// using the fields listed in the notification document.
			Vector recipientsFromFields = getBCCRecipientFields(d);
			for(Iterator i = recipientsFromFields.iterator();i.hasNext();){
				tmp.addElement((String)i.next());
				}
			// Get the recipients by user role(s)
			for(Iterator roles = doc.getItemValue(BCCRECIPIENT_ROLES_FIELD).iterator(); roles.hasNext();){
				String roleName = (String)roles.next();
				tmp.addElement(Approver.getNamesByRole(roleName, db));
			}	
			tmp = Approver.parseVec(tmp);
			if(sendToInternet()){tmp.addElement(getInternetAddresses(tmp));}
			tmp = Approver.parseVec(tmp);
			}catch(NotesException e){e.printStackTrace();}
		return tmp;
		}			
/** 
	* Determines whether the whether the 'attach Notes Doc link' is set to Yes or No for the notification document.
	* If the "attach Notes Doc Link" field is set to Yes in the notification configuration document, a Notes document to the context document
	* will be included at the bottom of each email notification sent.
	* @return A boolean true if the 'attach Notes Doc Link' field is set to yes, otherwise false.
**/

public boolean attachDocLink(){
		boolean tmp = false;
		try{
			if(doc.hasItem(ATTACH_DOCLINK_FIELD) && doc.getItemValueString(ATTACH_DOCLINK_FIELD) != null){
				if(doc.getItemValueString(ATTACH_DOCLINK_FIELD).equals(ATTACH_LINK_TRUE)){tmp=true;}
			}
			}catch(NotesException e){e.printStackTrace();}
			return tmp;
		}


/** 
	* Determines whether the whether the 'attach URL link' is set to Yes or No for the notification document.
	* If the attach "URL Link" field is set to Yes in the notification configuration document, a URL link to the context document
	* will be included at the bottom of each email notification sent.
	* @return A boolean true if the 'attach URL Link' field is set to yes, otherwise false.
**/

	public boolean attachLink(){
		boolean tmp = false;
		try{
			if(doc.getItemValueString(ATTACH_LINK_FIELD).equals(ATTACH_LINK_TRUE)){tmp=true;}
			}catch(NotesException e){e.printStackTrace();}
			return tmp;
		}
	/** 
	*  Gets the subject text from the notification configuration document.
	* @return A string , the subject text for the email notification that will be sent.
	* @param d  The context document
	**/
	public String getSubject(Document d){
		String tmp = null;
		try{
			tmp = doc.getItemValueString(MAIL_SUBJECT_FIELD);
			if(tmp.indexOf(">") != -1){
				tmp = Mail.subFieldValues(tmp, d);
			}
			}catch(NotesException e){e.printStackTrace();}
		return tmp;
	}	
	/** 
	*  Gets the bodytext from the notification configuration document.
	* @return A string , the body text for the email notification that will be sent.
	* @param d  The context document
	**/
	public String getBody(Document d, boolean useMIME){
		String tmp="";
		try{
			RichTextItem rt = (RichTextItem)doc.getFirstItem(MAIL_BODY_FIELD);
			tmp = rt.getUnformattedText();
			if(useMIME){
				if((tmp.indexOf(">") != -1) && (tmp.indexOf("<") != -1)){
					tmp = Mail.subFieldValues(tmp, d, useMIME);
				}
			}else{
				if((tmp.indexOf(">") != -1) && (tmp.indexOf("<") != -1)){
					tmp = Mail.subFieldValues(tmp, d);
				}
			}
			}catch(NotesException e){e.printStackTrace();}
		return tmp;
	}		
	/**
	*  Determines if the notification document specifies separate emails for each recipient
	**/
	private boolean sendSeparate(){
		boolean tmp = false;
		try{
			if( doc.hasItem(MAIL_SEND_SEPARATE_FIELD) && "Yes".equals(doc.getItemValueString(MAIL_SEND_SEPARATE_FIELD)) ){
				tmp = true;
				}
			}catch(NotesException e){e.printStackTrace(); }
		return tmp;
		}
	
	/** 
	*  Sends an email notification.
	* @param currentDoc  The context document
	**/
public void sendMail(Document currentDoc){
	Mail mail = new Mail(db, currentDoc);
	try{
		/*
		 * override the default global replyTo field with the replyTo field from the 
		 * current configuration document, if any.
		 */
		if(doc.hasItem(REPLY_TO_FIELD) && (doc.getItemValueString(REPLY_TO_FIELD) != null)){
			mail.setMailFrom(doc.getItemValueString(REPLY_TO_FIELD));
		}
	}catch(NotesException e){e.printStackTrace(); }
	boolean useMIME = useMIMEFormat();
	
	
	if(sendSeparate()){
		for(Iterator e=getRecipients(currentDoc).iterator(); e.hasNext() ; ){		
			Vector rec = new Vector(0);
			rec.addElement((String)e.next());
			mail.sendMail(
					rec, 
					getCCRecipients(currentDoc),  
					getBCCRecipients(currentDoc), 
					getSubject(currentDoc), 
					getBody(currentDoc, useMIME), 
					currentDoc, 
					attachLink(), 
					attachDocLink(), 
					useMIME,
					getImportance()
					);
		}
	}else{
		mail.sendMail(
				getRecipients(currentDoc), 
				getCCRecipients(currentDoc),  
				getBCCRecipients(currentDoc), 
				getSubject(currentDoc), 
				getBody(currentDoc, useMIME), 
				currentDoc, 
				attachLink(), 
				attachDocLink(), 
				useMIME,
				getImportance()
				);
	}
}
	/** 
	*  Determines if a given name is in the $User view of any of the address books for the current session or in any of the fake address books listed for the current application.
	* @return boolean true if the name is found in an address book, false otherwise.
	* @param name the name to be checked.
	**/
private boolean isNameValid(String name) throws RWFException{
	boolean tmp=false;
	Vector validationNabs = nabs;
	try{
		try{
			DBConfiguration c = new DBConfiguration(db);
			for(Iterator i = c.getFakeNabNames().iterator(); i.hasNext(); ){
				String fn = (String)i.next();
				if(fn != null){
					try{
						Database nab = db.getParent().getDatabase(db.getServer(), fn);
						if(!nab.isOpen()){nab.open();}{
							if(nab.isOpen()){
								validationNabs.addElement(nab);
							}
						}
						}catch(NotesException ex){
							throw new RWFException("Fake address book not found for filename " + fn, db);
							}
					
					}
		
				}
			}catch(ConfigDocNotFoundException e){e.printStackTrace();}
			

		for(Enumeration e=validationNabs.elements(); e.hasMoreElements(); ){
			Database nab = (Database)e.nextElement();
				if( !nab.isOpen() ){nab.open();}
				View v = nab.getView("($Users)");
				if(v != null){
				ViewEntry ve = v.getEntryByKey(name);
				if(ve!=null){
					tmp = true;
				}
			}
			
		}
	}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}
/**
* Use for address validation, this function generates a list of names that are not in any of the address books for the session of fake address books listed
* in the EMail configuration document (DBConfiguration class).
* The fieldnames to validate in the context document are stored in the notification configuration document in the field defined by ADDRESS_VAL_FIELDS_FIELD.
@return A Vector of strings containing any names not found for the fields in the context document as specified in the notification definition document.
@param currentDoc The context document.

**/
public Vector validateNames(Document currentDoc) throws RWFException{
	Vector namesNotFound = new Vector(0);
	try{
		if(doc.hasItem(ADDRESS_VAL_FIELDS_FIELD) && (doc.getItemValueString(ADDRESS_VAL_FIELDS_FIELD) != null)){
			Vector fieldNames = doc.getItemValue(ADDRESS_VAL_FIELDS_FIELD);
			for(Iterator i=fieldNames.iterator(); i.hasNext();){
				String fn = (String)i.next();
				if(currentDoc.hasItem(fn)){
					Vector names = currentDoc.getItemValue(fn);
					for(Enumeration e1=names.elements(); e1.hasMoreElements(); ){
						String name=(String)e1.nextElement();		
						if(!(isNameValid(name))){
							namesNotFound.addElement(name);		
							}
						}
					}
				}
		}
	}catch(NotesException e){e.printStackTrace();}
	return namesNotFound;
	}

public boolean hasExpired(Document d, Session session){
	boolean tmp = false;
	try{
		String stateName = getStateDocument().getItemValueString(State.STATE_NAME_FIELD);
		DateTime now = session.createDateTime("Today");
		now.setNow();
		Approver approver = new Approver(d);
		String timeForStatus = approver.getTimeForStatus(stateName);
		/// first, see if there is an expiration date field listed in the notification document 
		/// if there is, look in the context document and extract that date.
		DateTime expireDate = null;
		String expiredField = getExpiredDateFieldName();
		if(expiredField != ""){
			if(d.hasItem(expiredField)){
				Item item = d.getFirstItem(expiredField);
				if(item.getType() == Item.DATETIMES){
					expireDate = (DateTime)item.getValues().elementAt(0);
					}
				if(item.getType() == Item.TEXT){
					expireDate = session.createDateTime((String)item.getValues().elementAt(0));
					}
				if(expireDate.timeDifference(now) < 0){
					return true; }
				}
			}
		/// if there isn't an expiration date, check the expiration hours in the notification doc.
		if(doc.getItemValueDouble(RWF_ESCALATION_HOURS_FIELD) != 0.0 ) {
			double escalationHours = doc.getItemValueDouble(RWF_ESCALATION_HOURS_FIELD);
			if(timeForStatus != null){
				DateTime stateTime = session.createDateTime(timeForStatus);
				if(stateTime != null){
					double delta = now.timeDifference(stateTime);
					if(delta > (escalationHours)*(3600)){
						tmp=true;
						}
					}
				}	
			}
		}catch(NotesException e){ return tmp;}	
		 catch(Exception ex){return tmp;}
	return tmp;
	}
	
private String getExpiredDateFieldName(){
	String tmp = "";
	try{
		tmp = doc.getItemValueString(ESCALATION_EXPIRE_FIELD);
	}catch(NotesException e){e.printStackTrace(); return tmp; }	
	if(tmp == null){tmp="";}
	return tmp;
	}
	
/** 
	*  Sends any escalation notices required for the current application.
	*  This function is normally called by a scheduled agent.
	* @return boolean true if the name is found in an address book, false otherwise.
	* @param db The Database for which escalation notices are to be sent 
	* @param session The current session.
**/
public static void sendEscalationNotifications(Database db, Session session){
	try{
		View stateView = db.getView(State.STATE_VIEW_NAME);
		if(stateView == null){
			stateView = db.getView(State.STATE_VIEW_NAME_DEP);
			throw new RWFException("Warning view name '" + State.STATE_VIEW_NAME_DEP + "' has been deprecated, use view name '" + State.STATE_VIEW_NAME + "' instead.");
		}
		Document theDoc = stateView.getFirstDocument();
		while(theDoc != null){
			/// see if it's a response so we know it's a notification document
			if(theDoc.isResponse()){
				/// see if we have an escalation notification
				if((theDoc.getItemValueString(RWF_NOTIFICATION_TYPE_FIELD)!=null ) && (theDoc.getItemValueString(RWF_NOTIFICATION_TYPE_FIELD).equals("2"))){
					//// get the number of hours for the escalation
					/// get the state name that this notification applies to.
					String stateName = theDoc.getItemValueString(State.STATE_NAME_FIELD);
					String notificationName = theDoc.getItemValueString(NOTIFICATION_NAME_FIELD);
					String escalationName = stateName + notificationName;
					/// now go through all the document in the database and look for documents in this state
					DocumentCollection dc = db.getAllDocuments();
					Document d = dc.getFirstDocument();
					while(d != null){
						if(d.hasItem(Approver.RWF_STATUS_FIELD)){
							Vector stats = d.getItemValue(Approver.RWF_STATUS_FIELD);
							if((stats != null) && (stats.contains(stateName))){
									/// find out an escalation notification of the same name has already been sent.
									if((d.getItemValue(Approver.RWF_ESCALATION_NOTICES_FIELD)==null) || !(d.getItemValue(Approver.RWF_ESCALATION_NOTICES_FIELD)).contains(escalationName)){
									//// and finally see if the proper time has elapsed to send the notification
									Notification notification = new Notification(theDoc);
									if(notification.hasExpired(d, session)){
										/// send out the notification
										notification.sendMail(d);
										// update the source document so that we know this notification has been sent
										Vector notices= d.getItemValue(Approver.RWF_ESCALATION_NOTICES_FIELD);
										notices.addElement(escalationName);
										d.replaceItemValue(Approver.RWF_ESCALATION_NOTICES_FIELD, notices);
										Vector times = d.getItemValue(Approver.RWF_ESCALATION_TIMES_FIELD);
										times.addElement(session.createDateTime("Today"));
										d.replaceItemValue(Approver.RWF_ESCALATION_TIMES_FIELD, times);
										/// set the source document to the appropriate state, if necessary
										Vector newStates = notification.getEscalationStates();
										if(newStates != null){
											d.replaceItemValue(Approver.RWF_STATUS_FIELD, newStates);
											d.replaceItemValue(Approver.RWF_CHANGEFLAG_FIELD, "new status");
											WorkflowDocument wfd = new WorkflowDocument(d);
											wfd.setApprovers();
											wfd.setAuthors();
											wfd.setReaders();
											wfd.setHistory();
											wfd.resetChangeFlag();
											}
										d.save();
										}  // if hasExpired()
									} // if escaltion notice sent
								}  // contains StateName
							}  // hasItem RWF_STATUS
							d = dc.getNextDocument(d);
						} // while d != null
					}  // if
				} // if
				theDoc = stateView.getNextDocument(theDoc);
			} /// while
	
}catch(Exception e){e.printStackTrace(); }
}	

/**
* Function the gets the escalation states from a notification document.
* For reminder notifications the state of the context document will be changed 
* The value of the field RWF_ESCALATION_NEW_STATE_FIELD when the reminder
* notification is sent. 
@Return A Vector of Strings which are the names(s) of the new state(s) for the context document.
**/
public Vector getEscalationStates(){
	try{
		if(doc.getItemValueString(RWF_ESCALATION_NEW_STATE_FIELD) == null){
			return null;} 
		else {
			return doc.getItemValue(RWF_ESCALATION_NEW_STATE_FIELD); }
		}catch(NotesException e){e.printStackTrace(); }
	return null;
}

/**
 * Determines whether the message sent will be in plain text format or MIME text/html
 * @return true if the Notification configuration document indicates to use MIME formate, false otherwise.
 */
public boolean useMIMEFormat(){
	boolean tmp = false;
	try{
			if(doc.hasItem(MSG_FORMAT_FIELD)){
				if("2".equals(doc.getItemValueString(MSG_FORMAT_FIELD))){
					return true;
			}
		}
	}catch(NotesException e){
		// 
	}
	return tmp;
}

String getImportance(){
	String tmp = "Normal";
	try{
		tmp = doc.getItemValueString(MSG_IMPORTANCE_FIELD);
	}catch(NotesException e){}
	if(tmp == null){
		tmp = "Normal";
	}
	return tmp;
}
}