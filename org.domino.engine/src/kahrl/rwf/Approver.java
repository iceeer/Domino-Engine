package kahrl.rwf;
import lotus.domino.*;

import java.util.*;

/**
Description:  The approver class performs actions for the workflow engine that involve the context document.
Classes such as State, Notification and Subscription deal with interpreting business rules for the application and are associated with Domino Forms
in the application.  The Approver class bridges the gap between business rules and context documents being acted upon by the rules.
The Approver class is associated with the "rwf workflow" subform that is required to be in any document that is acted upon by the engine.
The Approver class also contains several useful static methods that perform functions such as parsing individual names from group lists or user roles, 
and nested Vectors.
@author Phillip Kahrl
**/

public class Approver {

private int VECTOR_INIT_SIZE = 0;
private Session ses;
private Database db;
private Document doc;

///The following are fields contained in the "rwf workflow" subform 
public static final String RWF_APPROVER_FIELD = "rwfDocApprovers";
public static final String RWF_APPROVER_NAMES_FIELD = "rwfDocApproverNames";
public static final String RWF_DELEGATES_FIELD = "rwfDelegates";
public static final String RWF_AUTHOR_FIELD = "rwfDocAuthors";
public static final String RWF_READER_FIELD = "rwfDocReaders";
public static final String RWF_STATUS_FIELD = "rwfDocStatus";
public static final String RWF_REMOVE_STATUS_FIELD = "rwfRemoveStatus";
public static final String RWF_REMOVE_APPROVER_FIELD = "rwfRemoveApprovers";
public static final String RWF_CHANGEFLAG_FIELD = "rwfHasChanged";
public static final String RWF_HISTORY_STATUS_FIELD = "rwfHistoryStatus";
public static final String RWF_HISTORY_TIME_FIELD = "rwfHistoryTime";
public static final String RWF_HISTORY_USER_FIELD = "rwfHistoryUser";
public static final String RWF_OLD_STATUS_FIELD = "rwfOldDocStatus";
public static final String RWF_INTIAL_AUTHOR_FIELD = "rwfInitialAuthor";
public static final  String RWF_DOCLINK_HINT_FIELD = "rwfDocLinkHint";
///  These fields define a view and field mapping to be used for approval delegation.
/// The view DELEGATE_VIEW_NAME should have full notes names of persons delegating approval as the first sorted column .
public static final String DELEGATE_VIEW_NAME_DEP = "rwf Approval Delegation";
public static final String DELEGATE_VIEW_NAME = "rwfApproval Delegation";
/// The following field name can be used to use an approval delegation view that is another database
public static final String RWF_DELEGATE_DB_NAME = "rwfApprovalDelegationDb";
public static final String RWF_ESCALATION_NOTICES_FIELD = "rwfEscalationNotification";
public static final String RWF_ESCALATION_TIMES_FIELD = "rwfEscalationTime";
/** The group view from the NAB **/
public static final String  GROUP_VIEW  = "($VIMGroups)";
/** field to capture user roles each time the document is saved from client/web **/
public static final String RWF_USER_ROLE_FIELD = "rwfUserRoles";
/**
Constructor for the Approver class
@param theDoc - the context document.
**/
public Approver(Document theDoc){
doc = theDoc;
try{
db = doc.getParentDatabase();
ses = db.getParent();
}catch(NotesException e){e.printStackTrace(); }
}
/**
Determines if a doc is being saved from the web or client
@return true if the doc is being saved from the web, false otherwise
@version 2.1
**/
public boolean fromWeb(){
	boolean tmp = true;
	try{
		if(doc.hasItem(RWF_USER_ROLE_FIELD)){
			Vector roles = doc.getItemValue(RWF_USER_ROLE_FIELD);
			if(!roles.contains("$$WebClient")){
				tmp = false;
				}	
			}
		}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}
/**
Checks the value of the change flag in the context document to determine if this document has changed and needs to be acted upon.
The change flag is set in the UI by changing the value of the field specified by RWF_CHANGEFLAG_FIELD
@return a boolean indicating whether the context document has changed status.
**/
public boolean hasChangedStatus(){
	boolean tmp = false;
	String NEW = "new status";
	String REMOVE = "remove status";
	try{
		String fieldvalue = doc.getItemValueString(RWF_CHANGEFLAG_FIELD);
		if(fieldvalue != null){
			tmp = (fieldvalue.equalsIgnoreCase(NEW) | fieldvalue.equalsIgnoreCase(REMOVE)); }
		}catch(NotesException e){e.printStackTrace(); }
	return tmp;
}
/**
Sets the approvers for the context document given a mixed list of names, grouplists or user roles.
This function handles approval delegation and also produces a list of individual approver names which will be stored in the field
with the fieldname=RWF_APPROVER_NAMES_FIELD
Approval delegation is non-exclusive for this method, meaning that both the approver and all delegates can approve.
@param approvers A list of approvers, can include grouplists or user roles as well as individual names.
**/
public void setApprovers(Vector approvers){
	setApprovers(approvers, false);
	}
/**
Sets the approvers for the context document given a mixed list of names, grouplists or user roles.
This function handles approval delegation and also produces a list of individual approver names which will be stored in the field
with the fieldname=RWF_APPROVER_NAMES_FIELD
@param approvers A list of approvers, can include grouplists or user roles as well as individual names.
@param exclusive if exclusive = true, then if an approver has a delegate, only the delegate can approve
if exclusive = false then both the approver and the delegates are approvers
**/
public void setApprovers(Vector approvers, boolean exclusive){
	approvers = parseVec(approvers);
	// remove empties
	while(approvers.contains("")){
		approvers.removeElement("");
		}
	//
	try{
		Item item = doc.getFirstItem(RWF_APPROVER_FIELD);
		doc.replaceItemValue(RWF_APPROVER_FIELD, canonicalizeNames(approvers));
		item.setAuthors(true);
		Vector tmp =   parseGroups((parseRoles(approvers, db)));
		Vector delegates = getDelegates(tmp, exclusive);
		if(exclusive){
			tmp = delegates;
		}else{			
			tmp.addElement(delegates);
		}
		doc.replaceItemValue(RWF_APPROVER_NAMES_FIELD, canonicalizeNames(parseVec(tmp)));
		doc.replaceItemValue(RWF_DELEGATES_FIELD, canonicalizeNames(delegates));
		doc.replaceItemValue(RWF_INTIAL_AUTHOR_FIELD ,"");
		}catch(NotesException e){e.printStackTrace(); }
 }
/**Sets the authors for the context document given a list of names, grouplists, or user roles.
@param authors A  Vector containing Strings or Vectors of Strings.  The Strings should be names, grouplist names or user roles.
**/
public void setAuthors(Vector authors){
	String fieldname = RWF_AUTHOR_FIELD;
	try{
		Item item = doc.getFirstItem(fieldname);
		doc.replaceItemValue(fieldname, canonicalizeNames(parseVec(authors)));
		item.setAuthors(true);
		}catch(NotesException e){e.printStackTrace();}
}
/**Sets the readers for the context document given a list of names, grouplists, or user roles.
@param readers A  Vector containing Strings or Vectors of Strings.  The Strings should be names, grouplist names or user roles.
**/	
public void setReaders(Vector readers){
	String fieldname = RWF_READER_FIELD;
	try{
		Item item = doc.getFirstItem(fieldname);
		doc.replaceItemValue(fieldname, canonicalizeNames(parseVec(readers)));
		item.setReaders(true);
		}catch(NotesException e){e.printStackTrace(); }
	}
/**Sets the context document to a new status.
@param status A state name, the new status of the document.
**/	
public void changeDocStatus(String status){
try{
	String fieldname = RWF_STATUS_FIELD;
     doc.replaceItemValue(fieldname, status);
   } catch(NotesException e){e.printStackTrace(); }
}

/**Given a user role, returns a list of names with that user role assigned in the given database.
@return A Vector of Strings containing names or grouplist names.
@param role A String for the role name, should be in square brackets.
@param theDb The current database.
**/	
public static Vector getNamesByRole(String role, Database theDb){
	Vector tmp = new Vector(1);
	Vector entryRoles = new Vector(1);
	try{
		ACL acl = theDb.getACL();
		ACLEntry entry = acl.getFirstEntry();
		while(entry != null){
			entryRoles = entry.getRoles(); 
			if(entryRoles.contains(role)){tmp.addElement(entry.getName());}
			entry = acl.getNextEntry(entry);
			}
				}catch(NotesException e){e.printStackTrace(); }
	return tmp;

}
/**Given a Vector of user roles, returns a list of names with that user role assigned in the current member Database.
@return A Vector of Strings containing names or grouplist names.
@param roles A Vector of Strings containing role names.
@param theDb The current database.
**/	

public Vector getNamesByRoles(Vector roles){
	Vector tmp = new Vector(VECTOR_INIT_SIZE);
	Vector entryRoles = new Vector(VECTOR_INIT_SIZE);
	String role = "";
	try{
		ACL acl = db.getACL();
		ACLEntry entry = acl.getFirstEntry();
		while(entry != null){
			entryRoles = entry.getRoles();
			for(Iterator i = entryRoles.iterator(); i.hasNext(); ){
				role = (String)i.next();
				if(roles.contains(role)){tmp.addElement(entry.getName());}
				}
			entry = acl.getNextEntry(entry);}
	}catch(NotesException e){e.printStackTrace(); }
 	return tmp;	
	}
/**Searches for user roles within a list of names, and replaces the role names with the users or groups to which the role is assigned.
This is the preferred method for parsing user roles.
@return A Vector of Strings containing names or grouplist names.
@param names A Vector of Strings containing a mixture of role names, user names or grouplist names.
@param theDb The current database.
**/	
public static Vector parseRoles(Vector names, Database theDb){
	names = parseVec(names);
	Vector tmp = new Vector(0);
	if((names == null) || (theDb == null)){return tmp; }
	String name="";
	for(Iterator i=names.iterator(); i.hasNext();){
		name = (String)i.next();
		if(name != null){
			if(name.startsWith("[")){
				tmp.addElement(getNamesByRole(name, theDb));}
			else{tmp.addElement(name);}
				}
			}
	return tmp;	
}
/**Another method for parsing roles, this is a member method, so you don't need to pass a database.
since current database is a class member.
@return A Vector of Strings containing names or grouplist names.
@param names A Vector of Strings containing a mixture of role names, user names or grouplist names.
**/	
public Vector parseRoles(Vector names){
	return parseRoles(names, db);
}

/**Returns a Vector of field values from a Vector of fieldnames in the context document.
All field values will be returned as a String.
@return A Vector of Strings.
@param fields A Vector of fieldnames in the context document.
**/
Vector getFieldValues(Vector fields){
String fieldname = "";
String val = "";
Vector fieldvalue = new Vector(VECTOR_INIT_SIZE);
Vector tmp = new Vector(VECTOR_INIT_SIZE);
try{
	for(Iterator i = fields.iterator(); i.hasNext(); ){
	fieldname = (String)i.next();
	fieldvalue = doc.getItemValue(fieldname);
	for(Iterator i1 = fieldvalue.iterator(); i1.hasNext();){
		val = (String)i1.next();
		tmp.addElement(val);	
	}  // for
}	// for
}catch(NotesException e){e.printStackTrace(); }
return tmp;
}  // getFieldValues
/**Returns a Vector of field values from a fieldname in the context document.
All field values will be returned as a String.
@return A Vector of Strings.
@param fieldName A fieldname in the context document.
**/
Vector getFieldValues(String fieldname){
	Vector tmp = new Vector(VECTOR_INIT_SIZE);
	try{	
		tmp = doc.getItemValue(fieldname);
		}catch(NotesException e){e.printStackTrace(); }
return tmp;
}
/**Returns all the current state names of the context document.
@return A Vector of Strings containing all of the current state names of the context document.
**/
public Vector getStatus(){
	Vector tmp = new Vector(1);
	try{
		tmp = doc.getItemValue(RWF_STATUS_FIELD);
	}catch(NotesException e){e.printStackTrace(); }
	return tmp;
	}
/**Resets the change flag field, used after the engine is done acting on a context document for workflow.
**/
public void resetChangeFlag(){
	String FALSE = "false";
	try{doc.replaceItemValue( RWF_CHANGEFLAG_FIELD, FALSE);
	     doc.replaceItemValue(RWF_REMOVE_STATUS_FIELD,"");
	     doc.replaceItemValue(RWF_REMOVE_APPROVER_FIELD,"");
	     doc.replaceItemValue(RWF_OLD_STATUS_FIELD, doc.getItemValue(RWF_STATUS_FIELD));
	     doc.replaceItemValue(RWF_USER_ROLE_FIELD,"");
		}catch(NotesException e){e.printStackTrace(); }
}
/**Sets the change flag back to the previous status, used when a document does not pass input email address validation.
**/
void revertStatus(){
	try{
		doc.replaceItemValue(RWF_STATUS_FIELD, doc.getItemValue(RWF_OLD_STATUS_FIELD));
	}catch(NotesException e){e.printStackTrace();}
}
/**Adds an entry to the history log for the given status.  Uses the current time and user name.
@param status A String to use for the status field of the history log.
**/
public void setHistory(String status){
	setHistory(status, true);
}
/**Adds an entry to the history log for the given status. Uses the current time and user name.
@param status A String to use for the status field of the history log.
@param addUniqueOnly Determines whether the status is added if it is already in the history log.  If true then
the status is added only if it is unique, if false then the status is added regardless of uniqueness.
**/
public void setHistory(String status, boolean addUniqueOnly){
		try{
			DateTime  today = ses.createDateTime("01/01/2002");
			today.setNow();
			setHistory(status, addUniqueOnly, today);
		}catch(NotesException e){e.printStackTrace();}
	}
/**Adds an entry to the history log for the given status and time. Uses the current user name.
@param status A String to use for the status field of the history log.
@param addUniqueOnly boolean determines whether the status is added if it is already in the history log.  If true then
the status is added only if it is unique, if false then the status is added regardless of uniqueness.
@param today The DateTime that will be used in the history log.
**/
public void setHistory(String status, boolean addUniqueOnly, DateTime today){
	String statusFieldname = RWF_HISTORY_STATUS_FIELD;
	String timeFieldname = RWF_HISTORY_TIME_FIELD;
	String userFieldname = RWF_HISTORY_USER_FIELD;
	Vector stat = new Vector(10);
	Vector time = new Vector(10);
	Vector user = new Vector(10);
	try{
		stat = doc.getItemValue(statusFieldname);
		if( !addUniqueOnly ||  !(stat.contains(status))){
			time = doc.getItemValue(timeFieldname);
			user = doc.getItemValue(userFieldname);
			stat.removeElement("");
			time.removeElement("");
			user.removeElement("");
			String now = today.getLocalTime();
			time.addElement(now);
			stat.addElement(status);
			String n = ses.getUserName();
			/// grab the username from the REMOTE_USER cgi variable for web apps.
			if(doc.hasItem("REMOTE_USER") && doc.getItemValueString("REMOTE_USER") != null){
				n = doc.getItemValueString("REMOTE_USER");	
				if(doc.getItemValueString("REMOTE_USER").trim().equals("")){n = "Anonymous";}	
			}
			user.addElement(n);
			doc.replaceItemValue(statusFieldname, stat);
			doc.replaceItemValue(timeFieldname, time);
			doc.replaceItemValue(userFieldname, user);
			} // if
	}catch(NotesException e){e.printStackTrace(); }	
	}
	/**Gets all the delegates for a given name.
	@param name a the name to get delegates for.
	@return a Vector of Strings containing a names of all delegates for the argument at the present time.
	**/
	public Vector getDelegates(String name){
		return getDelegates(name, false);		
		}
	/**Gets all the delegates for a given name.
	@param name the name to get delegates for.
	@param exclusive if exclusive is true then only delegates are returned, if false then both the original approver and delegates are returned.
	@return a Vector of Strings containing a names of all delegates for the argument at the present time.	
	**/
	public Vector getDelegates(String name, boolean exclusive){
		return getDelegates(name, exclusive, doc, ses, db);
	}
	
	public static Vector getDelegates(String name, boolean exclusive, Document doc, Session ses, Database db){
		Vector tmp = new Vector(0);
		String fieldname="";
		boolean foundDelegates = false;
		Database delDb = db;
		try{
			/// first see if the approval delegation view is specified for another database
			if(doc.hasItem(RWF_DELEGATE_DB_NAME) && (doc.getItemValueString(RWF_DELEGATE_DB_NAME) != null) && (doc.getItemValueString(RWF_DELEGATE_DB_NAME) != "")){
				delDb = ses.getDatabase(db.getServer(),doc.getItemValueString(RWF_DELEGATE_DB_NAME));
				if(!(delDb.isOpen())){delDb.open();}
			}
			
			View v = delDb.getView(DELEGATE_VIEW_NAME);
			if(v == null){
				v = delDb.getView(DELEGATE_VIEW_NAME_DEP);
				if(v != null){
					throw new RWFException("View name '" + DELEGATE_VIEW_NAME_DEP + "' is deprecated use view name'" + DELEGATE_VIEW_NAME + "' instead." );
				}
			}
			String key = ses.createName(name).getAbbreviated();
			Document d = v.getDocumentByKey(key, true);
			if(d!=null){
				DateTime today = ses.createDateTime("01/01/2000");
				today.setNow();
				for(int i=1; i<=20; i++){
					Vector vals = d.getItemValue("rwfStartDt_"+i);
					DateTime StartDt = (DateTime)vals.elementAt(0);
					vals = d.getItemValue("rwfEndDt_"+i);
					DateTime EndDt = (DateTime)vals.elementAt(0);
					if((today.timeDifference(StartDt)>=0) & (today.timeDifference(EndDt)<0)){
						foundDelegates = true;
						fieldname = "rwfDelName_" + i;
						tmp.addElement(d.getItemValueString(fieldname));						
						} // if
					} // for
				} // if
			doc = null;
			db = null;
			ses = null;
			}catch(Exception e){
				doc = null;
				db = null;
				ses = null;
				if(!foundDelegates & exclusive){
					tmp.addElement(name);
				} 
				return tmp; 
			}
		return tmp;
		}
	/**Gets all the delegates for a given list of names.
	@return a Vector of Strings containing a names of all delegates for the argument at the present time.
	@param name a Vector of Strings containing  the names to get delegates for.
	**/
	public Vector getDelegates(Vector names){
		return getDelegates(names, false);
		}
	/**Gets all the delegates for a given list of names.
	@return a Vector of Strings containing a names of all delegates for the argument at the present time.
	@param name a Vector of Strings containing  the names to get delegates for.
	@param exclusive boolean if true then only delegates are returned, if false then both the original approver and delegates are returned.
	**/
	public Vector getDelegates(Vector names, boolean exclusive){
	Vector tmp = new Vector(1);
	for(Iterator i = names.iterator(); i.hasNext();){
		tmp.addElement(getDelegates((String)i.next(), exclusive));
		}
	tmp = parseVec(tmp);
	return tmp;	
	}
/**Removes a status from the context document based on field values.  This method looks in the field in the context document 
* and removes any values in the field defined by RWF_REMOVE_STATUS_FIELD from the current state names of the context document.
**/
public void removeStatus(){
	try{
		if(doc.hasItem(RWF_CHANGEFLAG_FIELD) && (doc.getItemValueString(RWF_CHANGEFLAG_FIELD) != null)){
			String tmp = doc.getItemValueString(RWF_CHANGEFLAG_FIELD);
			if(tmp.equalsIgnoreCase("remove status")){
				if( doc.getItemValueString(RWF_REMOVE_STATUS_FIELD) != null ){
					tmp = doc.getItemValueString(RWF_REMOVE_STATUS_FIELD);
					removeStatus(tmp);
				}
				}
			}
		}catch(NotesException e){e.printStackTrace(); }
}
/**Removes a named status from the context document
@param status String, the status to be removed.
**/
public void removeStatus(String status){
	try{
	Vector stati = doc.getItemValue(RWF_STATUS_FIELD);
	stati.removeElement(status);
	
	if(stati.isEmpty()){
		
			try {
				State s = new State(doc, status);
				stati = s.getNextState();
			} catch (RWFException e) {
				e.printStackTrace();
			}
			
		
		}
	
	doc.replaceItemValue(RWF_STATUS_FIELD, stati);
	}catch(NotesException e){e.printStackTrace(); }
}

/**Given a mixed list of group names and person names, returns a list of person names by finding
all persons in any group in the list.  Currently works with nested grouplists to 3 levels.
This is a static method and requires a session as an argument.
@param groupnames, A Vector or nested Vector of Strings containing any mixture of names or grouplists.
@param s the current Session.
@return A Vector of Strings all names and names found in grouplists from the passed list.
**/
public static Vector parseGroups(Vector groupnames, Session s){
	/// parse nested groups 3 deep.
	return parseGroupsSingle(parseGroupsSingle(parseGroupsSingle(groupnames, s),s),s);
	}
/**Given a mixed list of group names and person names, returns a list of person names by finding
all persons in any group in the list.  Currently works with nested grouplists to 3 levels.
This performs the same function as the static method, but uses the member session.
@param groupnames A Vector or nested Vector of Strings containing any mixture of names or grouplists.
@return A Vector of Strings all names and names found in grouplists from the passed list.
**/	
public Vector parseGroups(Vector groupnames){
	return parseGroups(groupnames, ses);
	}
/**Given a mixed list of group names and person names, returns a list of person names by finding
all persons in any group in the list. This method parses only to one level.
This performs the same function as the static method of the same name, but uses the member session.
@param groupnames A Vector or nested Vector of Strings containing any mixture of names or grouplists.
@return A Vector of Strings all names and names found in grouplists from the passed list.
**/	
public Vector parseGroupsSingle(Vector groupnames){
	return parseGroups(groupnames, ses);
	}
/**Given a mixed list of group names and person names, returns a list of person names by finding
all persons in any group in the list. This method parses only to one level.
This is a static method and requires a Session to be passed.
@param groupnames A Vector or nested Vector of Strings containing any mixture of names or grouplists.
@return A Vector of Strings all names and names found in grouplists from the passed list.
**/	
public static Vector parseGroupsSingle(Vector groupnames, Session s){
	groupnames = parseVec(groupnames);
	Vector tmp = new Vector(1);
	String MEMBER_FIELD = "Members";
	try{
		Vector dirs = s.getAddressBooks();
		for(Iterator i=dirs.iterator(); i.hasNext();){
			Database dir = (Database)i.next();
			if(dir.open()){
			View v = dir.getView(GROUP_VIEW);
			if(v != null){
			LOOP_SEARCH_NAMES:for(Enumeration e1=groupnames.elements(); e1.hasMoreElements();){
				String groupname = (String)e1.nextElement();
				Document groupDoc = v.getDocumentByKey(groupname);
				if(groupDoc != null){
					tmp.addElement(groupDoc.getItemValue(MEMBER_FIELD));
					groupnames.removeElement(groupname);
					break LOOP_SEARCH_NAMES;
					} 
				}
			}
			} 
		}
	}catch(NotesException e){e.printStackTrace(); return parseVec(tmp);}
	tmp.addElement(groupnames);
	tmp = Approver.parseVec(tmp);
	return tmp;
	}	
/**This method parses a Vector that contains a mixture of Vectors and other objects (usually Strings). 
* It breaks out the elements of any contained Vector and returns a Vector containing only non-Vector objects.
@return A Vector that does not contain any Vectors.
@param A Vector that may contain a mixture of any object type or Vectors
**/
public static Vector parseVec(Vector v){
Vector tmp = new Vector(1);
for(Iterator i = v.iterator(); i.hasNext(); ){
	Object o = i.next();
	if(o instanceof Vector){
		Vector tmp1 = (Vector)o;
		for(Iterator i1 = tmp1.iterator(); i1.hasNext();){
			tmp.addElement(i1.next());
			}// for
			} // if
		else
		{tmp.addElement(o);}
	} // for
	boolean containsVec = false;
	for(Iterator i = v.iterator(); i.hasNext(); ){
			Object o = i.next();
			if(o instanceof Vector){containsVec=true;}
	}// for
	if(containsVec){return parseVec(tmp);}else{return tmp;}	
} // parseVec

/**Given a state name, this method will check the history log and determine when the context document transitioned into the passed state name.
@return String value of the DateTime that the Document arrived in the passed state.  If the passed State is not found in the history log, a null will be returned.
**/
public String getTimeForStatus(String status){
	String tmp = null;
	try{
		if(doc.hasItem(RWF_HISTORY_STATUS_FIELD)){
			Vector stats = doc.getItemValue(RWF_HISTORY_STATUS_FIELD);
			if(stats.contains(status)){
				Vector times = doc.getItemValue(RWF_HISTORY_TIME_FIELD);
				tmp = (String)times.elementAt(stats.indexOf(status));
				}
				}
			}catch(NotesException e){e.printStackTrace();}
		return tmp;
	}
/** Returns the value of the doc link hint from the context document
 * for use by the doc links in mail notification.  "Document" is returned
 * if the doc link field does not exist or is null. 
 *  
 *  */
public static String getDocLinkHint(Document theDoc){
	String tmp = "Document";
	try {
		tmp = theDoc.getItemValueString(RWF_DOCLINK_HINT_FIELD);
	} catch (NotesException e) {
		return tmp;
	}
	if(tmp == null){
		tmp = "Document";
	}
	return tmp;
}

/**Converts a Vector of names to Canonical format.
@return A Vector of Strings consisting of names in Canonical format.
@param names A Vector of Strings consisting of names.
**/
private Vector canonicalizeNames(Vector names){
	Vector tmp = new Vector(VECTOR_INIT_SIZE);
	String name = "";
		try{
			for(Iterator i = names.iterator(); i.hasNext(); ){
			name = (String)i.next();
			Name n = ses.createName(name);
			tmp.addElement(n.getCanonical());
			}	
}catch(NotesException e){e.printStackTrace(); }
return tmp;
	}
}  //  Class Approver