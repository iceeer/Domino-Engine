package kahrl.rwf;
import lotus.domino.*;
import java.util.*;
/**
This class is used to retrieve information for global email settings for the application, including base server URL, sender address
and locations of any fake address books for input validation.
**/
public class DBConfiguration {
/*
The following are used to tie this class into document settings in the database.
These settings can be changed if you wish to use a different view or document
structure to store your configuration settings.

*/
/** The Configuration document will be stored in a view with this name **/
public static final  String DB_CONFIG_VIEW_NAME = "rwfConfiguration";
/** The following key will be used to find the configuration document in the view. **/
public static final  String DB_CONFIG_KEY = "EMail";
/** The following is the field that will contain the mail setting. **/
public static final  String MAIL_TOGGLE_FIELD = "rwfEnableMail";
/** If the following value is set , mail memos will be sent, otherwise they will be saved. **/
public static final  String MAIL_TOGGLE_TRUE = "1";
/** The following field will be used for the Principal field on the mail, or the replyTo Address.**/
public static final  String MAIL_FROM_FIELD = "rwfEmailFrom";
/** The following used for the SMTPOriginator field in mail memos, will be the address for returned mails. **/
public static final  String MAIL_ORIGINATOR_FIELD = "rwfSMTPOriginator";
/** The following field contains the server URL that will be used for web browser links in the email notifications. **/
public static final  String SERVER_URL_FIELD = "rwfServerURL";
/** The following field controls whether mail is sent from this database or sent by saving in mail.box  **/
public static final String USE_MAILBOX_FIELD = "rwfUseMailBox";
/** The following field is used to add a fake address book for address validation **/
public static final String FAKE_NAB_LOC = "rwfFakeNabLoc";
/*
End of configuration settings.
*/
private Database db;
private Document configDoc;

/**
Constructor for the DBConfiguration class
@param theDb the current Database.
@param contextDoc the context document
@throws ConfigDocNotFoundException
**/
public DBConfiguration(Database theDb, Document contextDoc) throws ConfigDocNotFoundException{ 
	try{
		db = theDb;
		View v = db.getView(DB_CONFIG_VIEW_NAME);
		if(v==null){throw new ConfigDocNotFoundException(db, "Config view not found ", contextDoc);}
		else{
			configDoc = v.getDocumentByKey(DB_CONFIG_KEY, true); 
			if(configDoc == null){throw new ConfigDocNotFoundException(db, "Config doc not found for key:" + DB_CONFIG_KEY, contextDoc);}
		}
	}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
	}
/**
Constructor for the DBConfiguration class
@param theDb the current Database.
@throws ConfigDocNotFoundException
**/
public DBConfiguration(Database theDb) throws ConfigDocNotFoundException{ 
	try{
		db = theDb;
		View v = db.getView(DB_CONFIG_VIEW_NAME);
		if(v==null){throw new ConfigDocNotFoundException(db, "Config view not found ");}
		else{
			configDoc = v.getDocumentByKey(DB_CONFIG_KEY, true); 
			if(configDoc == null){throw new ConfigDocNotFoundException(db, "Config doc not found for key:" + DB_CONFIG_KEY);}
		}
	}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
	}
/**
* Retrieves parameter from the Email configuration document that determines how mail is sent.
* If the return value is true then mail will be sent by creating and saving a document in the mail router database (mail.box)
* If the return value is false then mail will be sent by using lotus.domino.Document.send() from the current application.
@return boolean 
**/
public boolean useMailBox(){
	boolean tmp = true;
	try{
		if(configDoc.hasItem(USE_MAILBOX_FIELD)){
			if(configDoc.getItemValueString(USE_MAILBOX_FIELD).equals("2")){
				tmp = false;
				}
			}
	}catch(NotesException e){e.printStackTrace();}
	return tmp;	
	}
/**
Retrieves the filenames of any fake address books to be used during address validation.
@return A Vector of strings containing the names of any fake address books.
**/
public Vector getFakeNabNames(){
	Vector tmp = new Vector(0);
	try{
	if(configDoc.hasItem(FAKE_NAB_LOC) && (configDoc.getItemValueString(FAKE_NAB_LOC) != null)){
		tmp=configDoc.getItemValue(FAKE_NAB_LOC);
	}
	}catch(NotesException e){e.printStackTrace();}
	return tmp;
	}
/**
Retrieves the mail from address.
@return String the mail from address
**/

	public String getMailFrom(){
	String tmp="";
	try{tmp = configDoc.getItemValueString(MAIL_FROM_FIELD);
		}catch(NotesException e){e.printStackTrace();}
	return tmp;
	}
/**
Retrieves the mail originator address.
@return String the mail originator address
**/
public String getMailOriginator(){
	String tmp="";
	try{tmp = configDoc.getItemValueString(MAIL_ORIGINATOR_FIELD);
		}catch(NotesException e){e.printStackTrace();}
	return tmp;
	}
/**
Retrieves the parameter that determines whether mail is actually sent or saved as a memo document in the current database.
@return true if mail will actually be sent, otherwise false.
**/	
public boolean sendMail(){
	boolean tmp = false;
	String s = "";
	try{
		s = configDoc.getItemValueString(MAIL_TOGGLE_FIELD);
}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
		
	if(s.equalsIgnoreCase(MAIL_TOGGLE_TRUE)){tmp=true;}
	return tmp;
	}
/**
Retrieves the base server URL for this application 
@return String the server URL specified for the current application.
**/
	public String getServerURL(){
		String tmp="/n";
		try{tmp = configDoc.getItemValueString(SERVER_URL_FIELD);
		}catch(NotesException e){e.printStackTrace();}
		return tmp;
		}	
}