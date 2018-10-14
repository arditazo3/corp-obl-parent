package com.tx.co.back_office.office.resource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.office.api.model.OfficeTaskTemplates;
import com.tx.co.back_office.office.api.model.OfficeTaskTemplatesResult;
import com.tx.co.back_office.office.api.model.TaskTempOfficesResult;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.service.IOfficeService;
import com.tx.co.common.api.provider.ObjectResult;

import static com.tx.co.common.constants.ApiConstants.*;

@Component
@Path(BACK_OFFICE)
public class OfficeResource extends ObjectResult {
	
	private static final Logger logger = LogManager.getLogger(OfficeResource.class);
	
	@Context
    private UriInfo uriInfo; 
	
	private IOfficeService officeService;

	@Autowired
	public void setOfficeService(IOfficeService officeService) {
		this.officeService = officeService;
	}
	
    @GET
    @Path(OFFICE_LIST)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(AUTH_ADMIN_FOREIGN_INLAND)
    public Response getOffices() {

        Iterable<Office> officeIterable = officeService.findAllOffice();
        List<OfficeResult> queryOfficeList =
                StreamSupport.stream(officeIterable.spliterator(), false)
                        .map(this::toOfficeResult)
                        .collect(Collectors.toList());

        return Response.ok(queryOfficeList).build();
    }
    
	@GET
    @Path(OFFICE_LIST_ROLE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfficesByRole() {

        Iterable<Office> officeIterable = officeService.getOfficesByRole();
        List<OfficeResult> queryOfficeList =
                StreamSupport.stream(officeIterable.spliterator(), false)
                        .map(this::toOfficeResult)
                        .collect(Collectors.toList());

        return Response.ok(queryOfficeList).build();
    }
    
    @POST
    @Path(OFFICE_CREATE_UPDATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(AUTH_ADMIN_FOREIGN_INLAND)
    public Response createUpdateOffice(OfficeResult officeResult) {

        Office officeStored = officeService.saveUpdateOffice(toOffice(officeResult));

        return Response.ok(toOfficeResult(officeStored)).build();
    }
    
    @PUT
    @Path(OFFICE_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(AUTH_ADMIN_FOREIGN_INLAND)
    public Response deleteOffice(OfficeResult office) {

    	officeService.deleteOffice(office.getIdOffice());

        return Response.noContent().build();
    }
    
    @POST
	@Path(SEARCH_OFFICE_TASKS)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(AUTH_ADMIN_FOREIGN_INLAND)
	public Response searchOfficeTaskTemplates(TaskTempOfficesResult taskTempOffices) {

		Iterable<OfficeTaskTemplates> officeTasksIterable = officeService.searchOfficeTaskTemplates(toTaskTempOffices(taskTempOffices));
        List<OfficeTaskTemplatesResult> queryDetailsList =
                StreamSupport.stream(officeTasksIterable.spliterator(), false)
                        .map(this::toOfficeTaskTemplates)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
	}
}
