package com.tx.co.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown if an authentication token is invalid.
 *
 * @author aazo
 */
public class InvalidAuthenticationTokenException extends AuthenticationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAuthenticationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
