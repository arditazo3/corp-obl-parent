package com.tx.co.admin.resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.tx.co.admin.service.IAdminService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.tx.co.common.constants.ApiConstants.*;

@Component
@Path(BACK_OFFICE)
@PreAuthorize(AUTH_ADMIN)
public class AdminResource {

	private IAdminService adminService;
	
	@Autowired
    public void setAdminService(IAdminService adminService) {
		this.adminService = adminService;
	}

	@GET
    @Path(REFRESH_CACHE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshCache() {
        
		adminService.refreshCache();
		
        return Response.noContent().build();
    }
}
