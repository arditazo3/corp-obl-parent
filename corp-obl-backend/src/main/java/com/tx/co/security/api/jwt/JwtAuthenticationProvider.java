package com.tx.co.security.api.jwt;

import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.service.AuthenticationTokenService;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Authentication provider for JWT token-based authentication.
 *
 * @author aazo
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
    public Authentication authenticate(Authentication authentication) {

        String authenticationToken = (String) authentication.getCredentials();
        AuthenticationTokenUserDetails authenticationTokenUserDetails = authenticationTokenService.parseToken(authenticationToken);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationTokenUserDetails.getUser().getUsername());

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Authority authority : authenticationTokenUserDetails.getUser().getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.name()));
        }
        
        return new JwtAuthenticationToken(userDetails, authenticationTokenUserDetails, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}