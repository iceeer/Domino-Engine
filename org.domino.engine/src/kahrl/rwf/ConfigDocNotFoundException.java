package kahrl.rwf;
import lotus.domino.*;
/**
Exception thrown if the Email configuration document for the current application can't be found.
**/
public class ConfigDocNotFoundException extends RWFException{
static final long serialVersionUID = -9191125231692875389L;	
/**
Constructor for ConfigDocNotFoundException
@param d - Database the current database
**/
ConfigDocNotFoundException(Database d){
	super("Config doc not found exception", d);
}
/**
Constructor for ConfigDocNotFoundException
@param d - Database the current database
@param msg - Error message String.
**/
ConfigDocNotFoundException(Database d, String msg){
	super(msg, d);
}
/**
ConfigDocNotFoundException constructor
This constructor is used to display an error page to the user on the web , if the error page exists
@param d - Database, the current database.
@param msg - String the error message.
@param contextDoc - Document the context document
**/
ConfigDocNotFoundException(Database d, String msg, Document contextDoc){
	super(msg, d);
     System.out.println("Show web error page");
	EXCEPTION_KEY = "ConfigDocNotFoundException";
	showExceptionWebPage(contextDoc);
	}
}
