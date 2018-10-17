package com.tx.co.back_office.user.api.resource;

import com.tx.co.abstraction.AbstractApiTest;
import com.tx.co.security.domain.Authority;
import com.tx.co.user.api.model.UserResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.tx.co.common.constants.ApiConstants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Tests for the user resource class.
 *
 * @author aazo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest extends AbstractApiTest {

    @Test
    public void getUsersAsAnonymous() {
        Response response = client.target(baseUri).path(USER).request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserListAsUser() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(baseUri).path(USER).path(USER_LIST).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserListAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path(USER).path(USER_LIST).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<UserResult> queryResults = response.readEntity(new GenericType<List<UserResult>>() {});
        assertNotNull(queryResults);
    }

    @Test
    public void getUserAsAnonymous() {

        String username = "ANONYMOUS";

        Response response = client.target(baseUri).path(USER).path(username).request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getAdminAsUser() {

        String username = "ADMIN";

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(baseUri).path(USER).path(username).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserAsAdmin() {

        String username = "USER";

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path(USER).path(username).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        UserResult queryResults = response.readEntity(UserResult.class);
        assertNotNull(queryResults);
        assertEquals(username, queryResults.getUsername());
    }

    @Test
    public void getAuthenticatedUserAsAnonymous() {

        Response response = client.target(baseUri).path(USER).path(ME).request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getAuthenticatedUserAsUser() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(baseUri).path(USER).path(ME).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        UserResult queryResults = response.readEntity(UserResult.class);
        assertNotNull(queryResults.getUsername());
        assertEquals("USER", queryResults.getUsername());
        assertThat(queryResults.getAuthorities(), containsInAnyOrder(Authority.CORPOBLIG_USER));
    }

    @Test
    public void getAuthenticatedUserAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path(USER).path(ME).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        UserResult queryResults = response.readEntity(UserResult.class);
        assertNotNull(queryResults.getUsername());
        assertEquals("ADMIN", queryResults.getUsername());
        assertThat(queryResults.getAuthorities(), containsInAnyOrder(Authority.CORPOBLIG_ADMIN));
    }
}
