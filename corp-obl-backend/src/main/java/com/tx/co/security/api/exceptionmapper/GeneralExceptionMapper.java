package com.tx.co.security.api.exceptionmapper;

import com.tx.co.common.api.model.ApiErrorDetails;
import com.tx.co.security.exception.GeneralException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;

public class GeneralExceptionMapper implements ExceptionMapper<GeneralException> {

    @Context
    private UriInfo uriInfo;

    /**
     * @param exception
     * @return the FORBIDDEN status of General Exception
     */
    @Override
    public Response toResponse(GeneralException exception) {

        Response.Status status = Response.Status.NOT_ACCEPTABLE;

        ApiErrorDetails errorDetails = new ApiErrorDetails();
        errorDetails.setStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage(exception.getMessage());
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(errorDetails).type(MediaType.APPLICATION_JSON).build();
    }
}