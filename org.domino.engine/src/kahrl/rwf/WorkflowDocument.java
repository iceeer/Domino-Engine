package kahrl.rwf;

import lotus.domino.*;
import java.util.*;
 /** Description: 
 *  This is the top level class for the rworkflow libraries.
 *  This class is intended to be used for writing agents using the library.
 *  WorkflowDocument integrates all other classes in the library into easy-to-use methods
 *
 * @author:  Phillip Kahrl
**/
public class WorkflowDocument{
	static final String VERSION_NAME = "3 beta";
	/**
     Notes field name used to open the document after address validation.  
	**/
	private String AV_RETURN_VIEW_NAME="avSourceView";
	/**
	Notes field used to write names not found in address validation to.
	**/
	private String AV_NAMES_NOT_FOUND_FIELD="rwfAVNamesNotFound";
	
	Vector approvers = new Vector(1);
	Vector authors = new Vector(1);
	Vector readers = new Vector(1);
	
	private Vector states = new Vector(0);
	private Vector subscriptions = new Vector(0);
	
	Approver approver;
	Document doc;
	
	/** 
	* This is the standard constructor for the class.  
    	* @param  d - The document context for the agent.
     **/
	public WorkflowDocument(Document d){
		doc = d;
		approver = new Approver(doc);
		try{
			subscriptions = Subscription.getSubscriptions(doc);
		}catch(RWFException e){e.printStackTrace();}
		//  make the current approvers, authors and readers available, even if the document hasn't changed status
		// except if the document is being closed from notes client (performance issue)
		if(approver.fromWeb() || approver.hasChangedStatus()){
			approver.removeStatus();
				 try{
				 	Vector stateNames = approver.getStatus();
				 	for(Iterator it = stateNames.iterator(); it.hasNext();){				 
				 		String status = (String)it.next();
				 		State s = new State(doc, status);
						states.addElement(s);
				 		approvers.addElement(s.getApprovers());
				 		authors.addElement(s.getAuthors());
				 		readers.addElement(s.getReaders());	
					}  // for
				 } catch(RWFException e){e.printStackTrace();}
			 }
	}
	/** 
	* Resets the change flag for the context document.  The change flag is a field that is set in the context document
	* to let the engine know that the document has changed state and needs to be acted on.  This needs to be reset
	* each time the document is changed to that the context document will not be acted on erroneously next time it is saved.
	 **/
	public void resetChangeFlag(){
		approver.resetChangeFlag();
		}
	/**
	* This function checks the field used by the change flag in the context document to see if the document has been changed.
	*  If the document has been changed then it needs to be acted on by the workflow methods
	**/		
	public boolean hasChangedStatus(){
		return approver.hasChangedStatus();
		}
	/**
	* Sets the approvers for the context document based on the current state(s) of the document.	
     * Approval delegation is not exclusive if this method is used meaning that if an approver has delegates, 
	* BOTH the original approver and any delegates are set as approvers.
	**/	
	public void setApprovers(){
 		approver.setApprovers(Approver.parseVec(approvers));
	}
	/**
	* Set the approvers for the context document based on the current state(s) of the document.
	@param exclusive -- Determines whether approval delegation is exclusive. If exclusive=true and an approver has set delegates, 
	* then only the delegates can approve, If exclusive=false both the approver and all delegates are set as approvers.
	**/
	public void setApprovers(boolean exclusive){
		approver.setApprovers(Approver.parseVec(approvers), exclusive);
		}
	/**
	Set the authors for the context document based on the current state(s). 
	**/	
	public void setAuthors(){
		approver.setAuthors(Approver.parseVec(authors));
	}
	/**
	Set the readers for the context document based on the current state(s). 
	**/	
	public void setReaders(){
		approver.setReaders(Approver.parseVec(readers));
	}
	/**
	* Sends mail for the context document for the states that it has arrived in.  This is for workflow mail notification only, 
	* the method sendSubscriptionMail() sends mail for any subscriptions that have been configured that apply to the context document.
	**/
	public void sendMail(){
		for(Iterator it=states.iterator(); it.hasNext(); ){
			State s = (State)it.next();
			s.sendMail(doc);
			}
	}
	/**
	Runs address validation as specified in the Notification documents for either subscriptions or states that apply to the context document.
	**/
	public boolean runValidation() {
		boolean tmp = true;
		String viewName = "0";
		try{
			/// clear the address validation names not found field before starting
			if(doc.hasItem(AV_NAMES_NOT_FOUND_FIELD)){doc.replaceItemValue(AV_NAMES_NOT_FOUND_FIELD,"");}
			/// get the alternate view name to open the document from if validation fails, if any
				if(doc.hasItem(AV_RETURN_VIEW_NAME) && (doc.getItemValueString(AV_RETURN_VIEW_NAME) != null)){
					viewName = doc.getItemValueString(AV_RETURN_VIEW_NAME);}
				Vector namesNotFound = new Vector(0);
				try{
					namesNotFound = getBogusNames();
				} catch(RWFException rwfex){}
				if(namesNotFound.size() > 0){
					tmp = false;
					doc.replaceItemValue(AV_NAMES_NOT_FOUND_FIELD, namesNotFound);
					doc.replaceItemValue("$$Return", "[/" + doc.getParentDatabase().getFilePath() + "/" + viewName + "/" + doc.getUniversalID() + "?EditDocument]"); 
					approver.revertStatus();
			}
			}catch(NotesException e){e.printStackTrace(); }
		return tmp;
		}
	/**
	* For address validation, this generates a list of names from the fields specified in the Notification document that can't be found in any 
	* of the address books.
	**/
	public Vector getBogusNames() throws RWFException{
		Vector bogusNames = new Vector(0);
		/// runs validation on fields containing names as specified in the Notification documents
		/// First get all the notification(s) for any states the workflow doc has
		for(Iterator it=states.iterator(); it.hasNext(); ){
			State s = (State)it.next();
			Vector notifications = s.getAllNotifications();
			for(Iterator it1=notifications.iterator(); it1.hasNext(); ){
				Notification n=(Notification)it1.next();
				Vector namesNotFound = n.validateNames(doc);
				if(namesNotFound.size() > 0){bogusNames.addElement(namesNotFound);}
				}
			}	
			/// Now get the Notifications for any subscriptions
			for(Iterator it=subscriptions.iterator(); it.hasNext(); ){
				Subscription s = (Subscription)it.next();
				Vector notifications = s.getAllNotifications();
				for(Iterator it1=notifications.iterator(); it1.hasNext(); ){
					Notification n=(Notification)it1.next();
					Vector namesNotFound = n.validateNames(doc);
				if(namesNotFound.size() > 0){bogusNames.addElement(namesNotFound);}
				}
			}	
	bogusNames = Approver.parseVec(bogusNames);	
	return bogusNames;	
	}
	/**
	Creates an entry in the document history for the given status. A status will only be listed once in the document history.
	@param - String status the status to be added to the history
	**/	

	public void setHistory(String status){
		approver.setHistory(status);
		}
    /**
    Sets history entries for the current state(s) of the document using state names for status.
    **/
	public void setHistory(){
		for(Iterator it=approver.getStatus().iterator(); it.hasNext(); ){
			approver.setHistory((String)it.next());
		}	
	}
	/**
	Sends all mail notifications related to a field change event in the context document.  This is a part of the Subscriptions function.
	**/
	public void sendFieldChangeMail(){
		try{
			Subscription.sendAllMail(doc);
		}catch(RWFException e){e.printStackTrace();}
	}
	/**
	Updates the document history with the name of the field change event which will be the same as the alias of the Subscription document for the event.
	**/
	public void setFieldChangeHistory(){
		/// update the edit history for all fields that have changed
		for(Iterator it = subscriptions.iterator(); it.hasNext(); ){
			Subscription s= (Subscription)it.next();
		approver.setHistory(s.getAlias(), false) ;
		}
	}
/**
* Sends out all email notifications for subscriptions that pertain to the context document.
* This includes notification for new documents, fields changed, or document edited.
**/
public void sendSubscriptionMail(){
	try{
		Vector subscriptions = Subscription.getSubscriptions(doc);
		for(Iterator it=subscriptions.iterator(); it.hasNext(); ){
			Subscription s = (Subscription)it.next();
			Vector notifications = s.getAllNotifications();
			for(Iterator notes=notifications.iterator(); notes.hasNext();){
				Notification n = (Notification)notes.next();
				n.sendMail(doc);
				}
		}	
} catch(RWFException x){x.printStackTrace();}
}
/**
* This writes the values of all fields referenced in subscription document for the current form to fields that 
* have the same field name with the suffix '_OLD' appended.  The engine compares the current values of the fields
* to the '_OLD' values when the document is saved to determine if field values have changed for Subscripiton events.
**/
public void writeLastRevision() throws RWFException{
	DocRevision.writeLastRevision(doc);
	}
/** Sends all reminder notifications for the current application
@param session the current Session
**/	
public static void sendReminderNotifications(Database db, Session session){
		Notification.sendEscalationNotifications(db, session);
		
}

/**  This method performs all necessary actions on a context document for both workflow and subscriptions.
*  This method should be called by any WebQuerySave or QueryClose agents acting upon a document where actions are required
*  for workflow, subscriptions, or keeping an edit history.
*  @Param doc - the context document passed from the WebQuerySave (or QueryClose) agent.
**/
public static void processContextDoc(Document doc){
//// Process the workflow stuff for the document	

	WorkflowDocument wfd = new WorkflowDocument(doc);
	boolean save = false;
	if(wfd.runValidation()){
		if(wfd.hasChangedStatus()){
			wfd.setApprovers();
			wfd.setAuthors();
			wfd.setReaders();
			wfd.sendMail();
			wfd.setHistory();
		}
/// Send mail for subscriptions 
		try {
			wfd.sendSubscriptionMail();
			// Write to the edit history
			wfd.setFieldChangeHistory();
			/// write the last doc revision to capture future field change events for subscriptions
			wfd.writeLastRevision();
			save = !wfd.fromWeb();
			wfd.resetChangeFlag();
		} catch (RWFException e) {
			e.printStackTrace();
		}
		  
		}
	try{	
		if(save){doc.save();}
	}catch(NotesException e){e.printStackTrace(); }
}
/**  Determines if the document is being saved from the web.  If it is then don't save (not necessary for the WebQuerySave event)
*  otherwise save since Notes client uses the QueryClose event/
@return true if the document is being save from the web, false otherwise
@version 2.1
**/
boolean fromWeb(){
	return approver.fromWeb();
	}

public static void selfTest(){
	System.out.println("RealWorkFlow self test ...");
}

public static String getVersionName(){
	return VERSION_NAME;
}
}

