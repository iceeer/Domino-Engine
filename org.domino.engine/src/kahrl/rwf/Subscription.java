package kahrl.rwf;
import java.util.*;
import lotus.domino.*;

/** Description: 
 *  This class is used to process subscriptions, or send email notifications when documents have been added or modified.
 *
 * @author:  Phillip Kahrl
**/

public class Subscription{
	/*
	Field mapping for the Notes form.
	*/
	public static final String SUBSCRIPTION_VIEW_NAME = "rwfSubscriptions";
	public static final String SUBSCRIPTION_FORM_NAME= "rwfSubscription";
	public static final String SUBSCRIPTION_FORMS_FIELD="rwfSubscriptionForms";
	public static final String SUBSCRIPTION_TYPE_FIELD= "rwfSubscriptionType";
	public static final String SUBSCRIPTION_TYPE_ADDED="1";	
	public static final String SUBSCRIPTION_TYPE_MODIFIED="2";	
	public static final String SUBSCRIPTION_TYPE_FIELDCHANGE="3";
	public static final String SUBSCRIPTION_FIELDS_FIELD = "rwfSubscriptionFieldNames";
	public static final String FIELDNAME_FIELD = "rwfSubscriptionFieldNames";
	public static final String ALIAS_FIELD = "rwfSubscriptionAlias";
	/// the following field is used to only send subscriptions for documents in specified states for newsletters
	public static final String SUBSCRIPTION_STATES_FIELD = "rwfSubscriptionStates";
	/* End of field mapping */
	/**  The subscription document **/
	private Document doc;
	/** Field names listed in this subscription document **/
	private Vector fieldName;
	/** The current database **/
	private Database db;
	/**
	Constructor for the subscription object.
	@param d - The subscription definition document
	**/
	public Subscription(Document d){
		doc = d;
		try{
			db = doc.getParentDatabase();
			fieldName = doc.getItemValue(FIELDNAME_FIELD);
			} catch(NotesException e){e.printStackTrace();}
		}
	/**
	Constructor for the subscription object by name
	@param  mdb - The current database
	@param subName - The name of the subscription
	**/	
	
	public Subscription(Database mdb, String subName) throws RWFException {
		db = mdb;
		View view = null;
		try{
			view = db.getView(SUBSCRIPTION_VIEW_NAME);
		}catch(NotesException e){
			throw new RWFException("Required view: " + SUBSCRIPTION_VIEW_NAME + " is missing in database: " + mdb, mdb);
		}
		if(view == null){
			throw new RWFException("Required view: " + SUBSCRIPTION_VIEW_NAME + " is missing in database: " + mdb, mdb);
		}
		try{
			doc = view.getDocumentByKey(subName);
			if(doc == null){
				throw new RWFException("Subscription document not found for name: " + subName, mdb); 
			}
			}catch(NotesException e){e.printStackTrace(); }
		}
	/**
	Get the fieldnames associated with this subscription
	@return - A Vector containing Strings which are fieldnames for this subscription.
	**/
	public Vector getFieldName(){return fieldName;}
	/** 
	Get the Subscription type
	@return a String of the subscription type will be an empty string or equal to SUBSCRIPTION_TYPE_MODIFIED, 
	SUBSCRIPTION_TYPE_ADDED, or SUBSCRIPTION_TYPE_FIELDCHANGE
	**/
	public Vector getType(){
		Vector tmp = null;
		try{
			tmp = doc.getItemValue(SUBSCRIPTION_TYPE_FIELD);
			}catch(NotesException e){e.printStackTrace(); }
		return tmp;
		}
     /**
     Gets the alias of this subscription
     @return A String, the alias.
     **/
	public String getAlias(){
		String tmp="";
		try{
			tmp = doc.getItemValueString(ALIAS_FIELD);			
			}catch(NotesException e){e.printStackTrace();}
		return tmp;
		}
	/**
	Retrieves a Vector of Notification objects based on the Notification definition documents that are children of the subscription definition document.
	@return - A Vector of Notification objects.
	**/
	public Vector getAllNotifications(){
		Vector tmp = new Vector(0);
		try{
		DocumentCollection dc = doc.getResponses();
		Document tmpDoc = dc.getFirstDocument();
		while(tmpDoc != null){
			if((!tmpDoc.hasItem(Notification.RWF_NOTIFICATION_TYPE_FIELD)) || (tmpDoc.getItemValueString(Notification.RWF_NOTIFICATION_TYPE_FIELD) != "2")){
				tmp.addElement(new Notification(tmpDoc));	
			}
			tmpDoc = dc.getNextDocument(tmpDoc);
			}
		}catch(NotesException e){e.printStackTrace();}
		return tmp;
		}
/**
Gets a set of Subscription objects based on fields that have changed in the context document.
@return - A Vector of Subscription objects.
@param - wfDoc The context document.
**/
public static Vector getFieldChangeSubscriptions(Document wfDoc) throws RWFException{
	// get the transition for fields that have changed
	Vector tmp = new Vector(0);
	try{
		Vector forms = wfDoc.getItemValue("Form");
		Vector subs = getSubDocsByForms(forms, wfDoc.getParentDatabase());
		for(Iterator it=subs.iterator(); it.hasNext() ; ){
			Document sub = (Document)it.next();
			if(sub.getItemValue(SUBSCRIPTION_TYPE_FIELD).contains(SUBSCRIPTION_TYPE_FIELDCHANGE)){
			Vector fieldNames = sub.getItemValue(SUBSCRIPTION_FIELDS_FIELD);
			try{
				if(DocRevision.itemsChanged(fieldNames, wfDoc).size()>0){	
					tmp.addElement(new Subscription(sub));
					}
			}catch(RWFException x){x.printStackTrace(); }
		}
}	
	}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}
/**
Gets a set of subscription objects based on a list of forms.
@param forms - A Vector of Strings - form names that the subscription definition documents must have in them to be returned by this method.
@param d - The current database.
**/
public static Vector getSubDocsByForms(Vector forms, Database d)throws RWFException{
	Vector tmp = new Vector(0);
	try{
		View subView = null;
		try{
			subView = d.getView(SUBSCRIPTION_VIEW_NAME);
		}catch(NotesException e){
			throw new RWFException("Required view: " + SUBSCRIPTION_VIEW_NAME + " is missing in database: " + d, d);
		}
		if(subView == null){
			throw new RWFException("Required view: " + SUBSCRIPTION_VIEW_NAME + " is missing in database: " + d, d);
		}
		Document doc = subView.getFirstDocument();
		while(doc != null){
			if(doc.getItemValueString("Form").equalsIgnoreCase(SUBSCRIPTION_FORM_NAME)){
				Vector subForms = doc.getItemValue(SUBSCRIPTION_FORMS_FIELD);
				for(Iterator it=subForms.iterator(); it.hasNext() ; ){
					if(forms.contains((String)it.next())){
						tmp.addElement(doc);
						}
					}
			}
			doc = subView.getNextDocument(doc);
		}
	}catch(NotesException e){e.printStackTrace() ; }
	return tmp;
	}
/**
Gets all subscriptions for new documents that match the form of the passed context document.
@return A Vector of Subscription objects
@param wfDoc - the context document
**/
public static Vector getNewSubscriptions(Document wfDoc) throws RWFException{
	Vector tmp = new Vector(0);
	try{
		Vector forms = wfDoc.getItemValue("Form");
		Vector subs = getSubDocsByForms(forms, wfDoc.getParentDatabase());
		for(Iterator it=subs.iterator(); it.hasNext() ; ){
			Document sub = (Document)it.next();
			if(sub.getItemValue(SUBSCRIPTION_TYPE_FIELD).contains(SUBSCRIPTION_TYPE_ADDED)){
				tmp.addElement(new Subscription(sub));
				}
		}	
	}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}
	
/**
Returns the forms associated with a subscription
@Return - Vector of String containing the form names, null if no forms are specified
**/
public Vector getForms(){
	Vector tmp = null;
	try{
		tmp = doc.getItemValue(SUBSCRIPTION_FORMS_FIELD);
	}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}
		
/**
Returns the states associated with a subscription
@Return - Vector of String containing the state  names, if any
**/
public Vector getStates(){
	Vector tmp = null;
	try{
		tmp = doc.getItemValue(SUBSCRIPTION_STATES_FIELD);
	}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}
	
/**
Gets a set of Subscription objects for modified type subscriptions that match the form name in the passed context document
@return A Vector of Subscription objects
@param wfDoc - the context document
**/
public static Vector getModifiedSubscriptions(Document wfDoc)throws RWFException{
	Vector tmp = new Vector(0);	
	try{
		Vector forms = wfDoc.getItemValue("Form");
		Vector subs = new Vector(0);
		try{
			subs = getSubDocsByForms(forms, wfDoc.getParentDatabase());
		}catch(RWFException x){
			x.printStackTrace();
		}
		for(Iterator it=subs.iterator(); it.hasNext() ; ){
			Document sub = (Document)it.next();
			if(sub.getItemValue(SUBSCRIPTION_TYPE_FIELD).contains(SUBSCRIPTION_TYPE_MODIFIED)){
				tmp.addElement(new Subscription(sub));
				}
		}	
	}catch(NotesException e){e.printStackTrace(); }
	return tmp;
}
/** gets all the subscriptions for the passed context document based on Form and subscription type.
This method wil determine if a document is new, modified, or has had fields changed and will return the correct set of Subscriptions.
@return A Vector of Subscription objects	
@param doc - the context document.
**/
public static Vector getSubscriptions(Document doc) throws RWFException{
	
	Vector tmp = new Vector(0);
	try{
		if(doc.isNewNote()){
			tmp = getNewSubscriptions(doc);
			}
		else{
			tmp = getFieldChangeSubscriptions(doc);
			tmp.addElement(getModifiedSubscriptions(doc));
			}
	}catch(NotesException e){e.printStackTrace(); }
	tmp = Approver.parseVec(tmp);
	/// now filter out the subscriptions that don't match the state of the context document
	for(Iterator it=tmp.iterator(); it.hasNext(); ){
		Subscription sub = (Subscription)it.next();
		if(!sub.stateMatch(doc)){tmp.removeElement(sub); }
		}
	return tmp;
	}
/**
Sends all mail related to Subscriptions for a given context document
@param wfDoc - the context document
@throws RWFException
**/
public static void sendAllMail(Document wfDoc) throws RWFException {
	Vector subscriptions = getSubscriptions(wfDoc);
	for(Iterator i = subscriptions.iterator(); i.hasNext();){
		Subscription s = (Subscription)i.next();
		Vector notifications = s.getAllNotifications();
		for(Iterator it = notifications.iterator(); it.hasNext();){
			Notification n = (Notification)it.next();
			n.sendMail(wfDoc); 
		}
	}
}
/**
Gets all Subscriptions in the current database associated with a given form name
@return A Vector of Subscription objects.
@param formName - String , the name of the form to look for in subscription definition documents.
@param db - Database, the current database.
**/
public static Vector getSubscriptionFieldsByForm(String formName, Database db) throws RWFException{
	Vector tmp = new Vector(0);
	Vector forms = new Vector(1);
	forms.addElement(formName);
	Vector subDocs = getSubDocsByForms(forms, db);
	for(Iterator it=subDocs.iterator(); it.hasNext(); ){
		Document subDoc = (Document)it.next();
		Subscription s = new Subscription(subDoc);
		tmp.addElement(s.getFieldName());
		
	} 
	return Approver.parseVec(tmp);
}

/**
* Determines if the states in a context document match the states in a subscripion document.
* @ return false if both the context document and the subscription both have states listed, but none of the states match, true otherwise 
*  
**/
boolean stateMatch(Document contextDoc) {
/// does the document state match the subscription
/// the state match is true if the subscription doesn't specify state(s) or if one of the subscription states matches the state found in the document
/// Phillip Kahrl 10-07-04 state match feature on hold until next release;
	return true;
	}
}