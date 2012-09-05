
package org.domino.engine.organization;


/**
 * Thrown when User/Group details are updated that cannot be changed by
 * underlying provider.
 *
 * @author 
 * @version 
 */
public class ImmutableException extends Exception {
	
	/**
	 * Default Constructor
	 */
    public ImmutableException() {
    }

    /**
     * Constructor
     * @param msg
     */
    public ImmutableException(String msg) {
        super(msg);
    }
}
