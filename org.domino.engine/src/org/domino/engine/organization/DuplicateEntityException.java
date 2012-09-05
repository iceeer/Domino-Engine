/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package org.domino.engine.organization;


/**
 * Signifies an entity cannot be created because
 * another entity already exists with the same name and type.
 *
 * @author 
 * @version 
 */
public class DuplicateEntityException extends Exception {

	/**
	 * Default Constructor
	 */
    public DuplicateEntityException() {
    }

    /**
     * Constructor
     * @param msg
     */
    public DuplicateEntityException(String msg) {
        super(msg);
    }
}
