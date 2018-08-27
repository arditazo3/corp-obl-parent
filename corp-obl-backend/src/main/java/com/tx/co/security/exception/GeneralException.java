package com.tx.co.security.exception;

/**
 * Thrown on any general exception.
 *
 * @author Ardit Azo
 */
public class GeneralException extends RuntimeException {

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }
}
