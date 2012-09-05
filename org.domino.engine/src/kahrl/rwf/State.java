package kahrl.rwf;
import lotus.domino.*;
import java.util.*;
/**
* This class deals with documents found in the view defined by STATE_VIEW_NAME.
* This class provides information about the business rules for worfklow states including who can approve, 
* edit and read documents in a given state.  
* This class is also responsible for sending mail notifications associated with a document arriving in a state by using
* notification documents which are child documents to the state document.
**/
public class State{
/*members */
/** The state definition document **/
private Document stateDoc;  
/** The context document  **/
private Document wfDoc;     
/**  The default size used when instantiating new Vector objects.  **/
private int VECTOR_INIT_SIZE = 1;
/** an approver object to be used by this state
 the approver object is used for getting information from the context document and for parsing user roles, grouplists, and Vectors.
**/
private Approver approver;  
/* Field mapping used in the state document
Change these only if the form used for state documents is also modified.
*/
public static final String STATE_NAME_FIELD = "rwfStateName";
public static final String NEXT_STATE_NAME_FIELD = "rwfNextStateName";
public static final String READER_TYPE_FIELD = "rwfReaderType";
public static final String READER_ROLES_FIELD = "rwfReaderUserRoles";
public static final String READER_NAMES_FIELD = "rwfReaderNames";
public static final String AUTHOR_TYPE_FIELD = "rwfAuthorType";
public static final String AUTHOR_ROLES_FIELD = "rwfAuthorUserRoles";
public static final String AUTHOR_NAMES_FIELD = "rwfAuthorNames";
public static final String APPROVER_TYPE_FIELD = "rwfApproverType";
public static final String APPROVER_ROLES_FIELD = "rwfApproverUserRoles";
public static final String APPROVER_NAMES_FIELD = "rwfApproveNames";
public static final String USER_APPROVER_FIELD = "rwfApproveFieldname";
public static final String USER_AUTHOR_FIELD = "rwfAuthorFieldname";
public static final String USER_READER_FIELD = "rwfReaderFieldname";
public static final String NEXT_STATE_PREFIX = "rwfChartState_";
/* End of state document field mapping */
/**  The view that contains state document and children */
/** deprecated state name view **/
public static final String STATE_VIEW_NAME_DEP = "rwf States";
public static final String STATE_VIEW_NAME = "rwfStates";
/**                                                                      
The constructor for the State object
@param theDoc - the context document.
@param status - name of the state to view, the state name is the key in the view defined by STATE_VIEW_NAME
**/
public State(Document theDoc, String status) throws RWFException {
	try{
	Database db = theDoc.getParentDatabase();
	wfDoc = theDoc;
	approver = new Approver(wfDoc);
	View states = db.getView(STATE_VIEW_NAME);
	try{
		if(states == null){
			states = db.getView(STATE_VIEW_NAME_DEP);
			throw new RWFException("Deprecation Warning:  view name 'rwf States' is deprecated, use view 'rwfStates' instead.");
		}
	}catch(RWFException rwfx){/* do nothing */ }
	if(states == null){throw new StateDocNotFoundException(wfDoc.getParentDatabase(),"State view:" + STATE_VIEW_NAME +" not found", theDoc);}
	stateDoc = states.getDocumentByKey(status, true);
	if(stateDoc == null){
		throw new StateDocNotFoundException(wfDoc.getParentDatabase(), "State doc not found for state: " + status, theDoc);}	
	}catch(NotesException e){e.printStackTrace();}
}
/**
Gets all the readers for the current state.
@return A Vector of Strings containings the names of all readers for the context document in this state
**/
public Vector getReaders(){
	Vector tmp = new Vector(VECTOR_INIT_SIZE);
	Vector type = new Vector(VECTOR_INIT_SIZE);
	try{
		type = stateDoc.getItemValue(READER_TYPE_FIELD);
		if(type.contains("1")){tmp.addElement(approver.getFieldValues(stateDoc.getItemValue(USER_READER_FIELD)));}
		if(type.contains("2")){tmp.addElement(stateDoc.getItemValue(READER_ROLES_FIELD));}
		if(type.contains("3")){tmp.addElement(stateDoc.getItemValue(READER_NAMES_FIELD));;}
		}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
		tmp = Approver.parseVec(tmp);
	return tmp;
}
/**
* Gets all the authors for the current context document in the this state
* Note.  Approvers are also authors in the Domino sense but are tracked separately.
@return A Vector of Strings containing names of all authors for the current state.
**/
public Vector getAuthors(){
	Vector tmp = new Vector(VECTOR_INIT_SIZE);
	Vector type = new Vector(VECTOR_INIT_SIZE);
	try{
		type = stateDoc.getItemValue(AUTHOR_TYPE_FIELD);
	if(type.contains("1")){tmp.addElement(approver.getFieldValues(stateDoc.getItemValue(USER_AUTHOR_FIELD)));}
		if(type.contains("2")){tmp.addElement(stateDoc.getItemValue(AUTHOR_ROLES_FIELD));}
		if(type.contains("3")){tmp.addElement(stateDoc.getItemValue(AUTHOR_NAMES_FIELD));;}
		}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
		tmp = Approver.parseVec(tmp);
	return tmp;
}
/**
Get all approvers for the current document in the current state.
An approver is defined as an author who can transition the context document to new states or remove states from the context document.
@retun A Vector of Strings which are names of approvers for the context document for this state.
**/
public Vector getApprovers(){
	Vector tmp = new Vector(VECTOR_INIT_SIZE);
	Vector type = new Vector(VECTOR_INIT_SIZE);
	try{
		type = stateDoc.getItemValue(APPROVER_TYPE_FIELD);
		if(type.contains("2")){tmp.addElement(stateDoc.getItemValue(APPROVER_ROLES_FIELD));}								   
		if(type.contains("1")){tmp.addElement(approver.getFieldValues(stateDoc.getItemValue(USER_APPROVER_FIELD)));}
		if(type.contains("3")){tmp.addElement(stateDoc.getItemValue(APPROVER_NAMES_FIELD));}	
	}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
	tmp = Approver.parseVec(tmp);
	return tmp;
}	
/**
Used for paralell approval processes, this method returns the name of the next state(s) that the context document should transition
to in the case that all current states are removed.
@return - A Vector of Strings which are the names of the next state(s) for the context document.
**/
public Vector getNextState(){
	Vector tmp = new Vector(1);
	try{
		tmp = stateDoc.getItemValue(NEXT_STATE_NAME_FIELD);
		}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
		return tmp;
	}
/**
Returns the Document that contains the definition (business rules) for this state - the state definition document.
@return Document - the state definition document for this state.
**/
public Document getStateDoc(){
return stateDoc;	
}
/**
Send all the mail notifications defined for when the context document enters this state
@param wfDoc - the context document
**/
public void sendMail(Document d){
	Vector notifications = getAllNotifications();
	for(Iterator it = notifications.iterator(); it.hasNext();){
		Notification n = (Notification)it.next();
		n.sendMail(d); 
		}
	}
/**
Send all the mail notifications defined for when a document enters this state.
**/
public void sendMail(){sendMail(wfDoc);}
/**
Gets all the Notification objects for this State.  This returns only Notifications that are for immediate use and does not include escalation notifications for this state.
@return A Vector of Notification objects.
**/		
public Vector getAllNotifications(){
	Vector tmp = new Vector(0);
	try{
		DocumentCollection dc = stateDoc.getResponses();
		Document tmpDoc = dc.getFirstDocument();
		while(tmpDoc != null){
			if(tmpDoc.hasItem(Notification.RWF_NOTIFICATION_TYPE_FIELD) && tmpDoc.getItemValueString(Notification.RWF_NOTIFICATION_TYPE_FIELD).equals("2")){
			// do nothing if it's an escation notification
			// otherwise add it to the notifications vector 
			}else{	
				tmp.addElement(new Notification(tmpDoc));	
			}
			tmpDoc = dc.getNextDocument(tmpDoc);
		}
	}catch(NotesException e){e.printStackTrace();}
	return tmp;
	}
/**
* Used for paralell approval, this returns the next state name for a given status.
* This method is used in paralell approval when all the current states for a context document have been removed.
* This is a static method and requires the current Database to be passed.
@param db the current database
@param status - String the name of the state.
**/		
public static Vector getNextStates(Database db, String status){
	Vector tmp = new Vector(10);
	Document doc = null;
	try{
		View v = db.getView(STATE_VIEW_NAME);
		if(v == null){
			v = db.getView(STATE_VIEW_NAME_DEP);
			throw new RWFException("Warning view name '" + STATE_VIEW_NAME_DEP + "' is deprecated use view name '" + State.STATE_VIEW_NAME + "' instead.");
		}
		doc = v.getDocumentByKey(status, true);
	for(int i=1;i<100;i++){
	String fieldname = NEXT_STATE_PREFIX + i;
			if(doc.hasItem(fieldname) ){
			if(doc.getItemValueString(fieldname)!=null){
		tmp.addElement(doc.getItemValue(fieldname));	}}}
		tmp.removeElement("");
		}catch(Exception e){e.printStackTrace(); }
return tmp;	
}
} // class State