package com.tx.co.security.api.resource;

import com.tx.co.abstraction.AbstractApiTest;
import com.tx.co.security.api.model.AuthenticationTokenUser;
import com.tx.co.security.api.model.UserCredentials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.tx.co.common.constants.ApiConstants.AUTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the authentication resource class.
 *
 * @author Ardit Azo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationResourceTest extends AbstractApiTest {

    @Test
    public void authenticateWithValidCredentials() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("ADMIN");
        //     credentials.setPassword("password");

        Response response = client.target(baseUri).path(AUTH).request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

        // Fail if the user exist on the database but doesn't have role associated
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        AuthenticationTokenUser authenticationToken = response.readEntity(AuthenticationTokenUser.class);
        assertNotNull(authenticationToken);
        assertNotNull(authenticationToken.getToken());

        // If user exist on the database and have at least one role associated
        assertNotNull(authenticationToken.getUser());
        assertNotNull(authenticationToken.getUser().getAuthorities());
    }

    @Test
    public void authenticateWithInvalidCredentials() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("invalid-user");
        // credentials.setPassword("wrong-password");

        Response response = client.target(baseUri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
}