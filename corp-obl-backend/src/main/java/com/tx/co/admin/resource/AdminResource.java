package com.tx.co.admin.resource;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@Path(USER)
@PreAuthorize(AUTH_ADMIN)
public class AdminResource {

	private static final Logger logger = LogManager.getLogger(AdminResource.class);
	
	private IAdminService adminService;
	
	@Autowired
    public void setAdminService(IAdminService adminService) {
		this.adminService = adminService;
	}

	@GET
    @Path(REFRESH_CACHE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshCache() {
        
		logger.info("refreshCache - Path: " + REFRESH_CACHE);
		
		adminService.refreshCache();
		
        return Response.noContent().build();
    }
}
