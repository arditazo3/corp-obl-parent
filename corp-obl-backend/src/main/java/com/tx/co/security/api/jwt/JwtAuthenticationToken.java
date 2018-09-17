package com.tx.co.security.api.jwt;

import com.tx.co.security.api.AuthenticationTokenUserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * An {@link org.springframework.security.core.Authentication} implementation designed for presentation of a JWT token.
 *
 * @author aazo
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String authenticationToken;
    private UserDetails userDetails;
    private AuthenticationTokenUserDetails authenticationTokenUserDetails;

    /**
     * Creates a {@link JwtAuthenticationToken} instance for an unauthenticated token.
     *
     * @param authenticationToken
     */
    public JwtAuthenticationToken(String authenticationToken) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.authenticationToken = authenticationToken;
        this.setAuthenticated(false);
    }

    /**
     * Creates a {@link JwtAuthenticationToken} instance for an authenticated token.
     *
     * @param userDetails
     * @param authenticationTokenUserDetails
     * @param authorities
     */
    public JwtAuthenticationToken(UserDetails userDetails, AuthenticationTokenUserDetails authenticationTokenUserDetails,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userDetails = userDetails;
        this.authenticationTokenUserDetails = authenticationTokenUserDetails;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted. Use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return authenticationToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public Object getDetails() {
        return authenticationTokenUserDetails;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.authenticationToken = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JwtAuthenticationToken that = (JwtAuthenticationToken) o;

        if (authenticationToken != null ? !authenticationToken.equals(that.authenticationToken) : that.authenticationToken != null)
            return false;
        if (userDetails != null ? !userDetails.equals(that.userDetails) : that.userDetails != null) return false;
        return authenticationTokenUserDetails != null ? authenticationTokenUserDetails.equals(that.authenticationTokenUserDetails) : that.authenticationTokenUserDetails == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (authenticationToken != null ? authenticationToken.hashCode() : 0);
        result = 31 * result + (userDetails != null ? userDetails.hashCode() : 0);
        result = 31 * result + (authenticationTokenUserDetails != null ? authenticationTokenUserDetails.hashCode() : 0);
        return result;
    }
}