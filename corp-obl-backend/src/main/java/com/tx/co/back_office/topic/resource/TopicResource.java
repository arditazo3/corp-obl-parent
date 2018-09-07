package com.tx.co.back_office.topic.resource;

import static com.tx.co.common.constants.AppConstants.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.topic.api.model.TopicResult;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.service.ITopicService;
import com.tx.co.common.api.provider.ObjectResult;

@Component
@Path(BACK_OFFICE)
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
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getTopics() {

        Iterable<Topic> topicIterable = topicService.findAllTopic();
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
    public Response createUpdateOffice(TopicResult topicResult) {

        Topic topicStored = topicService.saveUpdateTopic(toTopic(topicResult));

        return Response.ok(toTopicResult(topicStored)).build();
    }
	
}
