package com.tx.co.security.service.impl;

import com.tx.co.security.api.AuthenticatedUserDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.user.domain.User;
import com.tx.co.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation for the {@link UserDetailsService}.
 *
 * @author Ardit Azo
 */
@Service
public class DefaultUserDetailsService implements UserDetailsService {

    private final IUserService userService;

    @Autowired
    public DefaultUserDetailsService(IUserService userService) {
        this.userService = userService;
    }

    /**
     * @param username
     * @return the user logged
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        return new AuthenticatedUserDetails.Builder()
                .withUsername(user.getUsername())
                .withAuthorities(mapToGrantedAuthorities(user.getAuthorities()))
                .withActive(user.isEnabled())
                .build();
    }

    private Set<GrantedAuthority> mapToGrantedAuthorities(Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                .collect(Collectors.toSet());
    }
}