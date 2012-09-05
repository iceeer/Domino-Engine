
package org.domino.engine.organization;


/**
 * Thrown when User/Group is looked up by name that does not exist.
 *
 * @author 
 * @version 
 */
public class EntityNotFoundException extends Exception {
	
	/**
	 * Default Constructor
	 */
    public EntityNotFoundException() {
    }

    /**
     * Constructor
     * @param msg
     */
    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
