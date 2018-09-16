package com.tx.co.common.translation.resource;

import static com.tx.co.common.constants.ApiConstants.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tx.co.common.api.provider.ObjectResult;
import com.tx.co.common.translation.api.model.TranslationResult;
import com.tx.co.common.translation.domain.Translation;
import com.tx.co.common.translation.service.ITranslationService;

@Component
@Path(BACK_OFFICE)
public class TranslationResource extends ObjectResult {

	private static final Logger logger = LogManager.getLogger(TranslationResource.class);

	@Context
	private UriInfo uriInfo; 
	
	private ITranslationService translationService;

	@Autowired
	public void setTranslationService(ITranslationService translationService) {
		this.translationService = translationService;
	}
	
	@GET
    @Path(TRANSLATION_LIKE_TABLENAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getCompany(@QueryParam("tablename") String tablename, @QueryParam("lang") String lang) {

		Iterable<Translation> translationIterable = translationService.getTranslationLikeTablename(tablename, lang);
        List<TranslationResult> queryDetailsList =
                StreamSupport.stream(translationIterable.spliterator(), false)
                        .map(this::toTranslationResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
    }
}
