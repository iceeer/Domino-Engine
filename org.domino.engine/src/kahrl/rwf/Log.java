package kahrl.rwf;
import lotus.domino.*;

/**
* Description:  This class is used to Log messages from the sending of newsletters such as when a newsletter is sent and 
* error messages.  Logging is done by creating documents in the current database.
**/

public class Log {
// The name of the form and field to be used for logging.
public static final String LOG_FORM = "rwfLog";
public static final String LOG_BODY_FIELD = "body";
//  The current database and Document to be written to for logging.
private Database db;
private Document doc;

/**
* The log constructor.
@param d - The current Database.
**/
public Log(Database d){
	db = d;
	try{
		doc = db.createDocument();
		doc.replaceItemValue("Form", LOG_FORM);
	}catch(NotesException e){e.printStackTrace(); }
	}
/**
Writes a passed message to the current Log.
@param s - The message String to be written.
**/	
public void println(String s){
	try{
		String temp = doc.getItemValueString(LOG_BODY_FIELD);
		if(temp == null){temp = "";}
		doc.replaceItemValue(LOG_BODY_FIELD, temp + '\n' + '\n' + s);
		doc.save();
	}catch(NotesException e){e.printStackTrace(); }
	}

}
