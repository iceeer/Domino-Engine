/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package org.domino.engine.utility.number;

import com.topcoder.util.errorhandling.BaseException;


/**
 * <p>
 * Exception class for all number manage exceptions thrown from this API. It provides the ability to reference the underlying
 * exception via the <code>getCause</code> method, inherited from <code>BaseException</code>.
 * </p>
 *

 *
 * <p>
 * This exception will be created by a {@link NumberFactory} implementation if it encounters an exception creating a log.
 * Currently, no implementation throws this exception.
 * </p>
 *
 * <p>
 * <b>Thread safety: </b>
 * This exception is thread safe by having no mutable state.
 * </p>
 *
 */
public class NumberException extends BaseException {

    /**
     * <p>
     * Constructor takes a <tt>throwable</tt> as the underlying exception.
     * </p>
     *
     * @param throwable the underlying exception
     *
     * @since 1.2
     */
    public NumberException(Throwable throwable) {
        super(throwable);
    }

    /**
     * <p>
     * Constructs the exception from the specified message.
     * </p>
     *
     * <p>
     * <b>Changes from V2.0: </b>
     * This is a new constructor.
     * </p>
     *
     * @param msg A possibly null, possibly empty (trim'd) string exception message
     *
     * @since 2.0
     */
    public NumberException(String msg) {
        super(msg);
    }

    /**
     * <p>
     * Constructs the exception from the specified message and throwable.
     * </p>
     *
     * <p>
     * <b>Changes from V2.0: </b>
     * This is a new constructor.
     * </p>
     *
     * @param msg A possibly null, possibly empty (trim'd) string exception message
     * @param throwable A possibly null underlying exception
     *
     * @since 2.0
     */
    public NumberException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}

