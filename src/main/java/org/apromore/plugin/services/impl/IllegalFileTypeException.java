package org.apromore.plugin.services.impl;

/**
 * Thrown when an unknown file type is attempted to be uploaded.
 */
public class IllegalFileTypeException extends Exception {
    private static final long serialVersionUID = 1867207959897992342L;

    /**
     * Initialise exception.
     *
     * @param message message of the exception
     */
    public IllegalFileTypeException(String message) {
        super(message);
    }
}
