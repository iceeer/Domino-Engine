package kahrl.rwf;
import lotus.domino.*;
/**
Exception thrown when a specified field cannot be found in a the context documents
**/
public class FieldNotFoundException extends RWFException{
static final long serialVersionUID = 7433212673001074434L;
/**
PersonsNotFoundException constructor
@param  - d, the current database.
**/
public FieldNotFoundException(Database d){
	super("Field not found", d);
} 
/**
PersonsNotFoundException constructor
@param - msg, a String describing the error.
@param  - d, the context document.
**/
public FieldNotFoundException(String msg, Document d){
		super(msg, d);
	}
/**
PersonsNotFoundException constructor
@param - msg, a String describing the error.
@param  - d, the current database.
**/
public FieldNotFoundException(String msg, Database d){
	super(msg, d);
	}
/**
PersonsNotFoundException constructor
@param - msg, a String describing the error.
@param  - d, the current database.
@param - doc, the context document.
**/
public FieldNotFoundException(String msg, Database d, Document doc){
	super(msg, d, doc);	
	}
}