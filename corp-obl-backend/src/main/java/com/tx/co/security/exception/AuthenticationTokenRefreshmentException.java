package com.tx.co.security.exception;

/**
 * Thrown if an authentication token cannot be refreshed.
 *
 * @author aazo
 */
public class AuthenticationTokenRefreshmentException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticationTokenRefreshmentException(String message) {
        super(message);
    }

    public AuthenticationTokenRefreshmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
