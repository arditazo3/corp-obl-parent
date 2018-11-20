package com.tx.co.back_office.office.api.resource;

import static com.tx.co.common.constants.ApiConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tx.co.abstraction.AbstractApiTest;
import com.tx.co.back_office.office.api.model.OfficeResult;

/**
 * Tests for the office service class.
 *
 * @author aazo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfficeResourceTest extends AbstractApiTest {

	@Test
    public void getOffices() {

		String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());
		
		Response response = client.target(baseUri).path(BACK_OFFICE).path(OFFICE_LIST).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
		
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		List<OfficeResult> officeResults = response.readEntity(new GenericType<List<OfficeResult>>() {});
		
		assertNotNull(officeResults);

    }
}
