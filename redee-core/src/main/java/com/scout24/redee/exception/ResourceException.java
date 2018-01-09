package com.scout24.redee.exception;


/**
 * Exception used to signal problems during the execution of GATE controllers
 * and Processing Resources.
 * User: dprawdzik
 */
public class ResourceException extends Exception {

    /**
     * Constructor.
     */
    public ResourceException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message a message describing the exception / throwable and it's circumstances.
     */
    public ResourceException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param t simple Wrap-up off an given exception to conform throwable throw by the implemented interface or class.
     */
    public ResourceException(Throwable t) {
        super(t);
    }

    /**
     * Constructor.
     *
     * @param message a message describing the exception and it's circumstances.
     * @param t simple Wrap-up off an given exception to conform exception throw by the implemented interface or class.
     */
    public ResourceException(String
            message, Throwable t) {
        super(message, t);
    }
}
