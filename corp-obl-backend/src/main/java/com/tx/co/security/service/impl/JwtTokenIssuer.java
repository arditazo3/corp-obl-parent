package com.tx.co.security.service.impl;

import com.tx.co.security.api.AuthenticationTokenUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Component which provides operations for issuing JWT tokens.
 *
 * @author aazo
 */
@Component
public class JwtTokenIssuer {

    private final JwtSettings settings;

    @Autowired
    public JwtTokenIssuer(JwtSettings settings) {
        this.settings = settings;
    }

    /**
     * Issue a JWT token
     *
     * @param authenticationTokenUserDetails
     * @return
     */
    public String issueToken(AuthenticationTokenUserDetails authenticationTokenUserDetails) {

        return Jwts.builder()
                .setId(authenticationTokenUserDetails.getId())
                .setIssuer(settings.getIssuer())
                .setAudience(settings.getAudience())
                .setSubject(authenticationTokenUserDetails.getUser().getUsername())
                .setIssuedAt(Date.from(authenticationTokenUserDetails.getIssuedDate().toInstant()))
                .setExpiration(Date.from(authenticationTokenUserDetails.getExpirationDate().toInstant()))
                .claim(settings.getUserClaimName(), authenticationTokenUserDetails.getUser())
                .claim(settings.getRefreshCountClaimName(), authenticationTokenUserDetails.getRefreshCount())
                .claim(settings.getRefreshLimitClaimName(), authenticationTokenUserDetails.getRefreshLimit())
                .signWith(SignatureAlgorithm.HS256, settings.getSecret())
                .compact();
    }
}
