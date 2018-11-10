package com.tx.co.back_office.task.resource;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.task.api.model.TaskOfficeResult;
import com.tx.co.back_office.task.api.model.TaskResult;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.service.ITaskService;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateOfficeResult;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateResult;
import com.tx.co.common.api.provider.ObjectResult;

@Component
@Path(BACK_OFFICE)
@PreAuthorize(AUTH_ADMIN_FOREIGN_INLAND)
public class TaskResource extends ObjectResult {

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

		taskService.saveUpdateTask(toTaskWithTaskOffices(taskResult));

		return Response.noContent().build();
	}
	
	@GET
    @Path(TASK_LIST)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTasks() {

		Iterable<Task> taskIterable = taskService.getTasks();
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
    public Response getTaskOfficeByTaskTemplateAndOffice(TaskTemplateOfficeResult taskTemplateOffice) {

		TaskOffice taskOffice = taskService.
				getTaskOfficeByTaskTemplateAndOffice(toTaskTemplate(taskTemplateOffice.getTaskTemplate()), toOffice(taskTemplateOffice.getOffice()));

        return Response.ok(toTaskOfficeResult(taskOffice)).build();
    }
	
	@POST
    @Path(SINGLE_TASK_BY_TASKTEMPLATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSingleTaskByTaskTemplate(TaskTemplateResult taskTemplate) {

		Task task = taskService.
				getTasksByTaskTemplate(toTaskTemplate(taskTemplate));

        return Response.ok(toTaskResult(task)).build();
    }
	
    @PUT
    @Path(TASK_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTask(TaskResult taskResult) {

    	taskService.deleteTask(toTaskWithTaskOffices(taskResult));

        return Response.noContent().build();
    }
    
    @PUT
    @Path(TASK_OFFICE_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTaskOffice(TaskOfficeResult taskOfficeResult) {

    	taskService.deleteTaskOffice(toTaskOffice(taskOfficeResult));

        return Response.noContent().build();
    }
    
    
	
}
