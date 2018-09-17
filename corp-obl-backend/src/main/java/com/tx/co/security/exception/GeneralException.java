package com.tx.co.security.exception;

/**
 * Thrown on any general exception.
 *
 * @author aazo
 */
public class GeneralException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }
}
