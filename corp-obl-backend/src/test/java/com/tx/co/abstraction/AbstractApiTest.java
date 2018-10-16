package com.tx.co.abstraction;

import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.model.AuthenticationTokenUser;
import com.tx.co.security.api.model.UserCredentials;
import com.tx.co.security.authentication.AuthenticationTest;
import com.tx.co.security.service.AuthenticationTokenService;
import com.tx.co.user.domain.User;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.Collection;

import static com.tx.co.common.constants.ApiConstants.APP_PATH;
import static com.tx.co.common.constants.ApiConstants.AUTH;

/**
 * Base class for REST API testing.
 *
 * @author aazo
 */
@Component
public abstract class AbstractApiTest {

    @LocalServerPort
    protected int port;

    protected URI baseUri;

    protected Client client;
    
    private AuthenticationTokenService authenticationTokenService;
    
    @Autowired
    public void setAuthenticationTokenService(AuthenticationTokenService authenticationTokenService) {
		this.authenticationTokenService = authenticationTokenService;
	}

	@Before
    public void setUp() throws Exception {
        this.baseUri = new URI("http://localhost:" + port + "/" + APP_PATH);
        this.client = ClientBuilder.newClient();
    }

    protected String getTokenForUser() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("USER");
        // credentials.setPassword("password");

        AuthenticationTokenUser authenticationToken = client.target(baseUri).path(AUTH).request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationTokenUser.class);
        return authenticationToken.getToken();
    }

    protected String getTokenForAdmin() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("ADMIN");
        // credentials.setPassword("password");

        AuthenticationTokenUser authenticationToken = client.target(baseUri).path(AUTH).request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationTokenUser.class);
        return authenticationToken.getToken();
    }

    protected String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }
    
    protected void setAuthorizationUsername(String username) {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(username);

        AuthenticationTokenUser authenticationToken = client.target(baseUri).path(AUTH).request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationTokenUser.class);
        
        User userLogIn = authenticationToken.getUser();
        
        AuthenticationTokenUserDetails authenticationTokenUserDetails = new AuthenticationTokenUserDetails.Builder()
                .withId(null)
                .withUser(userLogIn)
                .withIssuedDate(null)
                .withExpirationDate(null)
                .withRefreshCount(1)
                .withRefreshLimit(1)
                .build();
        
        AuthenticationTest authenticationTest = new AuthenticationTest();
        authenticationTest.setDetails(authenticationTokenUserDetails);
        
        SecurityContextHolder.getContext().setAuthentication(authenticationTest);
    }
}
