package com.tx.co.security.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.exception.InvalidAuthenticationTokenException;
import com.tx.co.user.domain.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Component which provides operations for parsing JWT tokens.
 *
 * @author aazo
 */
@Component
public class JwtTokenParser {

    private final JwtSettings settings;

    @Autowired
    public JwtTokenParser(JwtSettings settings) {
        this.settings = settings;
    }

    /**
     * Parse a JWT token.
     *
     * @param token
     * @return
     */
    public AuthenticationTokenUserDetails parseToken(String token) {

        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(settings.getSecret())
                    .requireAudience(settings.getAudience())
                    .setAllowedClockSkewSeconds(settings.getClockSkew())
                    .parseClaimsJws(token)
                    .getBody();

            return new AuthenticationTokenUserDetails.Builder()
                    .withId(extractTokenIdFromClaims(claims))
                    .withUser(extractUserLogInFromClaims(claims))
                    .withIssuedDate(extractIssuedDateFromClaims(claims))
                    .withExpirationDate(extractExpirationDateFromClaims(claims))
                    .withRefreshCount(extractRefreshCountFromClaims(claims))
                    .withRefreshLimit(extractRefreshLimitFromClaims(claims))
                    .build();

        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
            throw new InvalidAuthenticationTokenException("Invalid token", e);
        } catch (ExpiredJwtException e) {
            throw new InvalidAuthenticationTokenException("Expired token", e);
        } catch (InvalidClaimException e) {
            throw new InvalidAuthenticationTokenException("Invalid value for claim \"" + e.getClaimName() + "\"", e);
        } catch (Exception e) {
            throw new InvalidAuthenticationTokenException("Invalid token", e);
        }
    }

    /**
     * Extract the token identifier from the token claims.
     *
     * @param claims
     * @return Identifier of the JWT token
     */
    private String extractTokenIdFromClaims(@NotNull Claims claims) {
        return (String) claims.get(Claims.ID);
    }

    /**
     * Extract the issued date from the token claims.
     *
     * @param claims
     * @return Issued date of the JWT token
     */
    private ZonedDateTime extractIssuedDateFromClaims(@NotNull Claims claims) {
        return ZonedDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault());
    }

    /**
     * Extract the expiration date from the token claims.
     *
     * @param claims
     * @return Expiration date of the JWT token
     */
    private ZonedDateTime extractExpirationDateFromClaims(@NotNull Claims claims) {
        return ZonedDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }

    /**
     * Extract the refresh count from the token claims.
     *
     * @param claims
     * @return Refresh count from the JWT token
     */
    private int extractRefreshCountFromClaims(@NotNull Claims claims) {
        return (int) claims.get(settings.getRefreshCountClaimName());
    }

    /**
     * Extract the refresh limit from the token claims.
     *
     * @param claims
     * @return Refresh limit from the JWT token
     */
    private int extractRefreshLimitFromClaims(@NotNull Claims claims) {
        return (int) claims.get(settings.getRefreshLimitClaimName());
    }

    /**
     * Extract the User from the token claims.
     *
     * @param claims
     * @return the user from the JWT token
     */
    private User extractUserLogInFromClaims(@NotNull Claims claims) {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(claims.get(settings.getUserClaimName()), User.class);
    }
}
