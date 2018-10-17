package com.tx.co.abstraction;

import com.tx.co.front_end.expiration.repository.ExpirationRepository;
import com.tx.co.front_end.expiration.service.ExpirationService;
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
	private ExpirationService expirationService;
	private ExpirationRepository expirationRepository;

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

	@Autowired
	public void setExpirationRepository(ExpirationRepository expirationRepository) {
		this.expirationRepository = expirationRepository;
	}

	public ExpirationRepository getExpirationRepository() {
		return expirationRepository;
	}

	@Autowired
	public void setExpirationService(ExpirationService expirationService) {
		this.expirationService = expirationService;
	}

	public ExpirationService getExpirationService() {
		return expirationService;
	}
}
