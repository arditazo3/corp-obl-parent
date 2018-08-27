package com.tx.co.security.api.model;

import com.tx.co.user.domain.User;

/**
 * API model for an authentication token.
 *
 * @author Ardit Azo
 */
public class AuthenticationTokenUser {

    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}