package com.tx.co.abstraction;

import org.springframework.beans.factory.annotation.Autowired;

import com.tx.co.back_office.task.repository.TaskRepository;
import com.tx.co.back_office.task.service.ITaskService;
import com.tx.co.back_office.tasktemplate.service.ITaskTemplateService;

/**
 * Base class for SERVICE testing.
 *
 * @author aazo
 */
public class AbstractServiceTest extends AbstractApiTest {

	private ITaskService taskService;
	private ITaskTemplateService taskTemplateService; 
	private TaskRepository taskRepository;

	public ITaskService getTaskService() {
		return taskService;
	}

	@Autowired
	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}

	public ITaskTemplateService getTaskTemplateService() {
		return taskTemplateService;
	}
	@Autowired
	public void setTaskTemplateService(ITaskTemplateService taskTemplateService) {
		this.taskTemplateService = taskTemplateService;
	}
	
	public TaskRepository getTaskRepository() {
		return taskRepository;
	}

	@Autowired
	public void setTaskRepository(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
}
