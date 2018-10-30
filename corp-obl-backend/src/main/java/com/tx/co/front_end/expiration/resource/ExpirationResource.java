package com.tx.co.front_end.expiration.resource;

import static com.tx.co.common.constants.ApiConstants.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.tx.co.common.api.provider.ObjectResult;
import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchivedResult;
import com.tx.co.front_end.expiration.api.model.ExpirationActivityResult;
import com.tx.co.front_end.expiration.api.model.ExpirationResult;
import com.tx.co.front_end.expiration.api.model.TaskOfficeExpirations;
import com.tx.co.front_end.expiration.api.model.TaskOfficeExpirationsResult;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;
import com.tx.co.front_end.expiration.service.IExpirationService;

@Component
@Path(FRONT_END)
@PreAuthorize(AUTH_ADMIN_USER)
public class ExpirationResource extends ObjectResult {

	@Context
	private UriInfo uriInfo; 

	private IExpirationService expirationService;

	@Autowired
	public void setExpirationService(IExpirationService expirationService) {
		this.expirationService = expirationService;
	}

	@POST
	@Path(SEARCH_AGENDA_TASK)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response searchDateExpirationOffices(DateExpirationOfficesHasArchivedResult dateExpirationOfficesArchived) {

		Iterable<TaskOfficeExpirations> taskTemplateExpirationsIterable = expirationService.
				searchDateExpirationOffices(toDateExpirationOfficesHasArchived(dateExpirationOfficesArchived));

		List<TaskOfficeExpirationsResult> queryDetailsList =
				StreamSupport.stream(taskTemplateExpirationsIterable.spliterator(), false)
				.map(this::toTaskOfficeExpirationsResult)
				.collect(Collectors.toList());

		return Response.ok(queryDetailsList).build();
	}
	
	@POST
	@Path(EXP_ACT_SAVE_UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveUpdateExpirationActivity(ExpirationActivityResult expirationActivity) {

		ExpirationActivity expirationActivityStored = expirationService.saveUpdateExpirationActivity(toExpirationActivity(expirationActivity));

        return Response.ok(toExpirationActivitySingleResult(expirationActivityStored)).build();
	}
	
	@POST
	@Path(STATUS_EXPIRATION_ON_CHANGE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response statusExpirationOnChange(ExpirationResult expiration) {

		Expiration expirationStored = expirationService.statusExpirationOnChange(toExpiration(expiration));

        return Response.ok(toExpirationWithActivitiesResult(expirationStored)).build();
	}
	
	@POST
	@Path(UPDATE_TASK_EXPIRATION)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTaskExpiration(TaskOfficeExpirationsResult taskOfficeExpirationsResult) {

		Expiration expirationStored = expirationService.statusExpirationOnChange(toTaskOfficeExpirations(taskOfficeExpirationsResult));

        return Response.ok(toExpirationWithActivitiesResult(expirationStored)).build();
	}
	
}
