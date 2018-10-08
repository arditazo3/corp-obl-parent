package com.tx.co.back_office.task.resource;

import static com.tx.co.common.constants.ApiConstants.*;

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

import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.task.api.model.TaskObjectTableResult;
import com.tx.co.back_office.task.api.model.TaskResult;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.service.ITaskService;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateOfficeResult;
import com.tx.co.back_office.topic.api.model.TopicResult;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.common.api.provider.ObjectResult;

@Component
@Path(BACK_OFFICE)
public class TaskResource extends ObjectResult {

	private static final Logger logger = LogManager.getLogger(TaskResource.class);
	
	@Context
	private UriInfo uriInfo; 
	
	private ITaskService taskService;

	@Autowired
	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}
	
	@POST
	@Path(TASK_CREATE_UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUpdateTask(TaskResult taskResult) {

		Task taskStored = taskService.saveUpdateTask(toTaskWithTaskOffices(taskResult));

//		return Response.ok(toTaskResult(taskStored)).build();
		return Response.noContent().build();
	}
	
	@GET
    @Path(TASK_LIST)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getTasks() {

		Iterable<Task> taskIterable = taskService.getTasks();
        List<TaskResult> queryDetailsList =
                StreamSupport.stream(taskIterable.spliterator(), false)
                        .map(this::toTaskResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
    }
	
	@POST
    @Path(TASK_DESC_COMP_TOPIC)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getTasksByDescriptionOrCompaniesOrTopics(TaskObjectTableResult taskObjectTableResult) {

		String description = taskObjectTableResult.getDescription();
		List<CompanyResult> companies = taskObjectTableResult.getCompanies();
		List<TopicResult> topics = taskObjectTableResult.getTopics();
		
		List<Company> companyList =
                StreamSupport.stream(companies.spliterator(), false)
                        .map(this::toCompany)
                        .collect(Collectors.toList());
		
		List<Topic> topicList =
                StreamSupport.stream(topics.spliterator(), false)
                        .map(this::toTopic)
                        .collect(Collectors.toList());
		
		Iterable<Task> taskIterable = taskService.getTasksByDescriptionOrCompaniesOrTopics(description, companyList, topicList);
        List<TaskResult> queryDetailsList =
                StreamSupport.stream(taskIterable.spliterator(), false)
                        .map(this::toTaskResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
    }
	
	@POST
    @Path(TASK_TEMPLATE_OFFICE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getTaskOfficeByTaskTemplateAndOffice(TaskTemplateOfficeResult taskTemplateOffice) {

		TaskOffice taskOffice = taskService.
				getTaskOfficeByTaskTemplateAndOffice(toTaskTemplate(taskTemplateOffice.getTaskTemplate()), toOffice(taskTemplateOffice.getOffice()));

        return Response.ok(toTaskOfficeResult(taskOffice)).build();
    }
	
}
