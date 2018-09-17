package com.tx.co.security.api.exceptionmapper;

import com.tx.co.common.api.model.ApiErrorDetails;
import com.tx.co.security.exception.AuthenticationTokenRefreshmentException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link AuthenticationTokenRefreshmentException}s.
 *
 * @author aazo
 */
@Provider
public class AuthenticationTokenRefreshmentExceptionMapper implements ExceptionMapper<AuthenticationTokenRefreshmentException> {

    @Context
    private UriInfo uriInfo;

    /**
     * @param exception
     * @return the FORBIDDEN status of Authentication Token Refreshment Exception
     */
    @Override
    public Response toResponse(AuthenticationTokenRefreshmentException exception) {

        Status status = Status.FORBIDDEN;

        ApiErrorDetails errorDetails = new ApiErrorDetails();
        errorDetails.setStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage("The authentication token cannot be refreshed.");
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(errorDetails).type(MediaType.APPLICATION_JSON).build();
    }
}