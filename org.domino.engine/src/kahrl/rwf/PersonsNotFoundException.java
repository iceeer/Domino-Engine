package kahrl.rwf;
import lotus.domino.*;
/**
Exception thrown when a name specified for a readers or authors field cannot be found in the NAB.
**/
public class PersonsNotFoundException extends RWFException {
static final long serialVersionUID = 300728177569363365L;
/**
PersonsNotFoundException constructor
@param  - d, the current database.
**/
public PersonsNotFoundException (Database d){
	super("Persons not found .", d);
}
/**
PersonsNotFoundException constructor
@param - msg, String the error message to be used.
@param  - d, the current database.
**/
public PersonsNotFoundException(Database d, String msg){
	super(msg, d);
}	

}