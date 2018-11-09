package com.tx.co.back_office.topic.resource;

import static com.tx.co.common.constants.ApiConstants.*;

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

import com.tx.co.back_office.company.api.model.CompanyTopicResult;
import com.tx.co.back_office.topic.api.model.TopicConsultantResult;
import com.tx.co.back_office.topic.api.model.TopicResult;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;
import com.tx.co.back_office.topic.service.ITopicService;
import com.tx.co.common.api.provider.ObjectResult;

@Component
@Path(BACK_OFFICE)
@PreAuthorize(AUTH_ADMIN_FOREIGN_INLAND_USER_ADMIN)
public class TopicResource extends ObjectResult {

	private static final Logger logger = LogManager.getLogger(TopicResource.class);
	
	@Context
    private UriInfo uriInfo; 
	
	private ITopicService topicService;

	@Autowired
	public void setTopicService(ITopicService topicService) {
		this.topicService = topicService;
	}
	
    @GET
    @Path(TOPIC_LIST)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTopics() {

    	logger.info("getTopics - Path: " + TOPIC_LIST);
    	
        Iterable<Topic> topicIterable = topicService.getTopicsByRoleList();
        List<TopicResult> queryTopicList =
                StreamSupport.stream(topicIterable.spliterator(), false)
                        .map(this::toTopicResult)
                        .collect(Collectors.toList());

        return Response.ok(queryTopicList).build();
    }
    
    @GET
    @Path(TOPIC_LIST_ROLE)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(AUTH_ADMIN_FOREIGN_INLAND)
    public Response getTopicsByRole() {

    	logger.info("getTopicsByRole - Path: " + TOPIC_LIST_ROLE);
    	
        Iterable<Topic> topicIterable = topicService.getTopicsByRole();
        List<TopicResult> queryTopicList =
                StreamSupport.stream(topicIterable.spliterator(), false)
                        .map(this::toTopicResult)
                        .collect(Collectors.toList());

        return Response.ok(queryTopicList).build();
    }    
    
    
    
    @POST
    @Path(TOPIC_CREATE_UPDATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUpdateTopic(TopicResult topicResult) {

    	logger.info("createUpdateTopic - Path: " + TOPIC_CREATE_UPDATE);
    	
        Topic topicStored = topicService.saveUpdateTopic(toTopicWithTranslation(topicResult));

        return Response.ok(toTopicResult(topicStored)).build();
    }
    
    @PUT
    @Path(TOPIC_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTopic(TopicResult topic) {

    	logger.info("deleteTopic - Path: " + TOPIC_DELETE);
    	
    	topicService.deleteTopic(topic.getIdTopic());

        return Response.noContent().build();
    }

    @POST
    @Path(TOPIC_CONSULTANT_CREATE_UPDATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUpdateTopicConsultant(TopicConsultantResult topicConsultantResult) {

    	logger.info("createUpdateTopicConsultant - Path: " + TOPIC_CONSULTANT_CREATE_UPDATE);
    	
    	TopicConsultant topicConsultantStored = topicService.saveUpdateTopicConsultant(toTopicConsultant(topicConsultantResult));

        return Response.ok(toTopicConsultantResult(topicConsultantStored)).build();
    }
    
    @PUT
    @Path(TOPIC_CONSULTANT_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTopicConsultant(TopicConsultantResult topicConsultantResult) {

    	logger.info("deleteTopicConsultant - Path: " + TOPIC_CONSULTANT_DELETE);
    	
    	topicService.deleteTopicConsultant(toTopicConsultant(topicConsultantResult));

        return Response.noContent().build();
    }
    
    @PUT
    @Path(TOPIC_CONSULTANTS_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTopicConsultants(CompanyTopicResult companyTopicResult) {

    	logger.info("deleteTopicConsultants - Path: " + TOPIC_CONSULTANTS_DELETE);
    	
    	topicService.deleteTopicConsultants(toCompany(companyTopicResult.getCompany()), toTopicWithTranslation(companyTopicResult.getTopic()));

        return Response.noContent().build();
    }    
}
