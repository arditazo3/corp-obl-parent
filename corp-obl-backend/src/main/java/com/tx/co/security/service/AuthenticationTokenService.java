package com.tx.co.security.service;

import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.user.domain.User;

/**
 * Service which provides operations for authentication tokens.
 *
 * @author Ardit Azo
 */
public interface AuthenticationTokenService {

    /**
     * Issue an authentication token for a user with the given authorities.
     *
     * @param user
     * @return
     */
    String issueToken(User user);

    /**
     * Parse an authentication token.
     *
     * @param token
     * @return
     */
    AuthenticationTokenUserDetails parseToken(String token);

    /**
     * Refresh an authentication token.
     *
     * @param currentAuthenticationTokenUserDetails
     * @return
     */
    String refreshToken(AuthenticationTokenUserDetails currentAuthenticationTokenUserDetails);
}
