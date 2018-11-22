package com.tx.co.back_office.tasktemplate.resource;

import static com.tx.co.common.constants.ApiConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

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
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.api.model.TaskResult;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.service.ITaskService;
import com.tx.co.back_office.tasktemplate.api.model.ObjectSearchTaskTemplateResult;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateOfficeResult;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateResult;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplate.service.ITaskTemplateService;
import com.tx.co.common.api.model.StringResult;
import com.tx.co.common.api.provider.ObjectResult;
import com.tx.co.common.utils.UtilStatic;

@Component
@Path(BACK_OFFICE)
@PreAuthorize(AUTH_ADMIN_FOREIGN_INLAND)
public class TaskTemplateResource extends ObjectResult {

	private static final Logger logger = LogManager.getLogger(TaskTemplateResource.class);

	@Context
	private UriInfo uriInfo; 

	private ITaskTemplateService taskTemplateService;
	private ITaskService taskService;

	@Autowired
	public void setTaskTemplateService(ITaskTemplateService taskTemplateService) {
		this.taskTemplateService = taskTemplateService;
	}

	@Autowired
	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}

	@GET
    @Path(TASK_TEMPLATE_LIST_FOR_TABLE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasAuthority('"+ ADMIN_ROLE + "') or hasAuthority('" + FOREIGN_ROLE + "')")
	public Response getTaskTemplatesForTable() {

		logger.info("getTaskTemplatesForTable - Path: " + TASK_TEMPLATE_LIST_FOR_TABLE);
		long startTime = System.currentTimeMillis();
		
		Iterable<Task> taskForTableIterable = taskTemplateService.getTasksForTable();
        List<TaskResult> queryDetailsList =
                StreamSupport.stream(taskForTableIterable.spliterator(), false)
                        .map(this::toTaskResult)
                        .collect(Collectors.toList());

        UtilStatic.DurationExecutionMethod(startTime, "getTaskTemplatesForTable", logger);
        
        return Response.ok(queryDetailsList).build();
    }
	
	@POST
	@Path(TASK_TEMPLATE_CREATE_UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUpdateTaskTemplate(TaskTemplateOfficeResult taskTemplateOfficeResult) {

		logger.info("createUpdateTaskTemplate - Path: " + TASK_TEMPLATE_CREATE_UPDATE);
		long startTime = System.currentTimeMillis();
		
		TaskTemplateResult taskTemplateRequest = taskTemplateOfficeResult.getTaskTemplate();
		OfficeResult officeRequest = taskTemplateOfficeResult.getOffice();
		Office office = officeRequest == null ? null : toOffice(officeRequest);
		TaskResult taskRequest = taskTemplateOfficeResult.getTask();
		Task task = taskRequest == null ? null : toTaskWithTaskOffices(taskRequest.setTaskTemplateReturnTask(taskTemplateRequest));
		Boolean isSavingTaskTemplateTask = taskTemplateOfficeResult.getIsSavingTaskTemplateTask();
		
		TaskTemplate taskTemplateStored = taskTemplateService.saveUpdateTaskTemplate(toTaskTemplate(taskTemplateRequest), office);

		if (isSavingTaskTemplateTask && !isEmpty(task)) {
			task.setTaskTemplate(taskTemplateStored);
			taskService.saveUpdateTask(task);
		}
		
		UtilStatic.DurationExecutionMethod(startTime, "createUpdateTaskTemplate", logger);
		
		return Response.ok(toTaskTemplateWithTaskResult(taskTemplateStored)).build();
	}
	
	
    @PUT
    @Path(TASK_TEMPLATE_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTaskTemplate(TaskTemplateResult taskTemplateResult) {

    	logger.info("deleteTaskTemplate - Path: " + TASK_TEMPLATE_DELETE);
    	
    	taskTemplateService.deleteTaskTemplate(toTaskTemplate(taskTemplateResult));

        return Response.noContent().build();
    }
	
	@POST
	@Path(TASK_TEMPLATE_SEARCH)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasAuthority('"+ ADMIN_ROLE + "') or hasAuthority('" + FOREIGN_ROLE + "')")
	public Response searchTaskTemplateTable(ObjectSearchTaskTemplateResult objectSearchTaskTemplateResult) {

		logger.info("searchTaskTemplateTable - Path: " + TASK_TEMPLATE_SEARCH);
		
		Iterable<Task> taskForTableIterable = taskTemplateService.searchTaskTemplate(toObjectSearchTaskTemplate(objectSearchTaskTemplateResult));
        List<TaskResult> queryDetailsList =
                StreamSupport.stream(taskForTableIterable.spliterator(), false)
                        .map(this::toTaskResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
	}
	
	@POST
	@Path(TASK_TEMPLATE_SEARCH_BY_DESCR)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response searchTaskTemplateTable(StringResult description) {

		logger.info("searchTaskTemplateTable - Path: " + TASK_TEMPLATE_SEARCH_BY_DESCR);
		
		List<TaskTemplate> taskTemplateIterable = taskTemplateService.searchTaskTemplateByDescr(description.getResult());
        List<TaskTemplateResult> queryDetailsList =
                StreamSupport.stream(taskTemplateIterable.spliterator(), false)
                        .map(this::toTaskTemplateResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
	}

}
