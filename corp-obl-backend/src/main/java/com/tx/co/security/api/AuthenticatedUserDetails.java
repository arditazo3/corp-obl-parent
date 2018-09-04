package com.tx.co.security.api;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Default implementation for the {@link UserDetails} interface.
 *
 * @author Ardit Azo
 */
public final class AuthenticatedUserDetails implements UserDetails, CredentialsContainer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean active;

    private AuthenticatedUserDetails(String username, Set<GrantedAuthority> authorities, boolean active) {
        this.username = username;
        this.authorities = Collections.unmodifiableSet(authorities);
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public void eraseCredentials() {
    }

    /**
     * Builder for the {@link UserDetails}.
     */
    public static class Builder {

        private String username;
        private Set<GrantedAuthority> authorities;
        private boolean active;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withAuthorities(Set<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public AuthenticatedUserDetails build() {
            return new AuthenticatedUserDetails(username, authorities, active);
        }
    }
}
