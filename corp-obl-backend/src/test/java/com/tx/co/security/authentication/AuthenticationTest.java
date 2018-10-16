package com.tx.co.security.authentication;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationTest implements Authentication {

	private String name;
	private Collection<? extends GrantedAuthority> authorities;
	private Object credentials;
	private Object details;
	private Object principal;
	private boolean authenticated;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	public Object getCredentials() {
		return credentials;
	}
	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
	public Object getDetails() {
		return details;
	}
	public void setDetails(Object details) {
		this.details = details;
	}
	public Object getPrincipal() {
		return principal;
	}
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
	public boolean isAuthenticated() {
		return authenticated;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
}
