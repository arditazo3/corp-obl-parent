package com.tx.co.admin.resource;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("admin") // TODO Ardit: create costant
@PreAuthorize("JustAdmin") // TODO Ardit: create costant for admin access
public class AdminResource {

    @GET
    @Path("refreshCache") // TODO Ardit: create costant
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshCache() {
        // TODO Ardit: refresh cache
        return Response.noContent().build();
    }
}
