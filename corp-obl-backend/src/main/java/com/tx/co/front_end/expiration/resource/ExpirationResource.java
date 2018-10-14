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
import com.tx.co.front_end.expiration.api.model.TaskTemplateExpirations;
import com.tx.co.front_end.expiration.api.model.TaskTemplateExpirationsResult;
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

		Iterable<TaskTemplateExpirations> taskTemplateExpirationsIterable = expirationService.
				searchDateExpirationOffices(toDateExpirationOfficesHasArchived(dateExpirationOfficesArchived));

		List<TaskTemplateExpirationsResult> queryDetailsList =
				StreamSupport.stream(taskTemplateExpirationsIterable.spliterator(), false)
				.map(this::toTaskTemplateExpirations)
				.collect(Collectors.toList());

		return Response.ok(queryDetailsList).build();
	}
}
