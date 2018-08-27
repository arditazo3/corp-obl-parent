package com.tx.co.security.api.jwt;

import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.service.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Authentication provider for JWT token-based authentication.
 *
 * @author Ardit Azo
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final AuthenticationTokenService authenticationTokenService;

    @Autowired
    public JwtAuthenticationProvider(UserDetailsService userDetailsService, AuthenticationTokenService authenticationTokenService) {
        this.userDetailsService = userDetailsService;
        this.authenticationTokenService = authenticationTokenService;
    }

    /**
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String authenticationToken = (String) authentication.getCredentials();
        AuthenticationTokenUserDetails authenticationTokenUserDetails = authenticationTokenService.parseToken(authenticationToken);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationTokenUserDetails.getUser().getUsername());

        return new JwtAuthenticationToken(userDetails, authenticationTokenUserDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}