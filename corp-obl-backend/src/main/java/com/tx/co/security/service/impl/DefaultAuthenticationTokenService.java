package com.tx.co.security.service.impl;

import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.exception.AuthenticationTokenRefreshmentException;
import com.tx.co.security.service.AuthenticationTokenService;
import com.tx.co.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Default implementation for the {@link com.tx.co.security.service.AuthenticationTokenService}.
 *
 * @author Ardit Azo
 */
@Service
public class DefaultAuthenticationTokenService implements AuthenticationTokenService {

    /**
     * How long the token is valid for (in seconds).
     */
    @Value("${authentication.jwt.validFor}")
    private Long validFor;

    /**
     * How many times the token can be refreshed.
     */
    @Value("${authentication.jwt.refreshLimit}")
    private Integer refreshLimit;

    private final JwtTokenIssuer tokenIssuer;

    private final JwtTokenParser tokenParser;

    @Autowired
    public DefaultAuthenticationTokenService(JwtTokenIssuer tokenIssuer, JwtTokenParser tokenParser) {
        this.tokenIssuer = tokenIssuer;
        this.tokenParser = tokenParser;
    }

    /**
     * @param user
     * @return issue the Token
     */
    @Override
    public String issueToken(User user) {

        String id = generateTokenIdentifier();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenUserDetails authenticationTokenUserDetails = new AuthenticationTokenUserDetails.Builder()
                .withId(id)
                .withUser(user)
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withRefreshCount(0)
                .withRefreshLimit(refreshLimit)
                .build();

        return tokenIssuer.issueToken(authenticationTokenUserDetails);
    }

    @Override
    public AuthenticationTokenUserDetails parseToken(String token) {
        return tokenParser.parseToken(token);
    }

    /**
     * @param currentTokenDetails
     * @return refresh the Token
     */
    @Override
    public String refreshToken(AuthenticationTokenUserDetails currentTokenDetails) {

        if (!currentTokenDetails.isEligibleForRefreshment()) {
            throw new AuthenticationTokenRefreshmentException("This token cannot be refreshed.");
        }

        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenUserDetails newTokenDetails = new AuthenticationTokenUserDetails.Builder()
                .withId(currentTokenDetails.getId()) // Reuse the same id
                .withUser(currentTokenDetails.getUser())
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withRefreshCount(currentTokenDetails.getRefreshCount() + 1)
                .withRefreshLimit(refreshLimit)
                .build();

        return tokenIssuer.issueToken(newTokenDetails);
    }

    /**
     * Calculate the expiration date for a token.
     *
     * @param issuedDate
     * @return ZonedDateTime
     */
    private ZonedDateTime calculateExpirationDate(ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(validFor);
    }

    /**
     * Generate a token identifier.
     *
     * @return the Token
     */
    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}

