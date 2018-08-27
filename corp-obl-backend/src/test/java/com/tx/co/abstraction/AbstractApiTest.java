package com.tx.co.abstraction;

import com.tx.co.security.api.model.AuthenticationTokenUser;
import com.tx.co.security.api.model.UserCredentials;
import org.junit.Before;
import org.springframework.boot.web.server.LocalServerPort;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.net.URI;

import static com.tx.co.common.constants.AppConstants.APP_PATH;
import static com.tx.co.common.constants.AppConstants.AUTH;

/**
 * Base class for REST API testing.
 *
 * @author Ardit Azo
 */
public abstract class AbstractApiTest {

    @LocalServerPort
    protected int port;

    protected URI baseUri;

    protected Client client;

    @Before
    public void setUp() throws Exception {
        this.baseUri = new URI("http://localhost:" + port + "/" + APP_PATH);
        this.client = ClientBuilder.newClient();
    }

    protected String getTokenForUser() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("CORPOBLIG_USER");
        // credentials.setPassword("password");

        AuthenticationTokenUser authenticationToken = client.target(baseUri).path(AUTH).request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationTokenUser.class);
        return authenticationToken.getToken();
    }

    protected String getTokenForAdmin() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("CORPOBLIG_ADMIN");
        // credentials.setPassword("password");

        AuthenticationTokenUser authenticationToken = client.target(baseUri).path(AUTH).request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationTokenUser.class);
        return authenticationToken.getToken();
    }

    protected String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }
}
