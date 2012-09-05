package kahrl.rwf;
import lotus.domino.*;
/**
Exception thrown when the engine is given a document state name for which no matching state document can be found.
**/
public class StateDocNotFoundException extends RWFException {
static final long serialVersionUID = -5917179110843398451L;
/**
StateDocNotFoundException constructor
@param d - The current Database
**/
StateDocNotFoundException(Database d){
	super("State doc not found", d);
}
/**
StateDocNotFoundException constructor
@param d - The current Database
@param msg - The error message.
**/
StateDocNotFoundException(Database d, String msg){
	super(msg, d);
}	
/**
StateDocNotFoundException constructor
This constructor is used to display an error page to the user on the web , if the error page exists
@param d - Database, the current database.
@param msg - String the error message.
@param contextDoc - Document the context document
**/
StateDocNotFoundException(Database d, String msg, Document contextDoc){
super(msg, d);
  	EXCEPTION_KEY = "StateDocNotFoundException";
	showExceptionWebPage(contextDoc);
	}

}