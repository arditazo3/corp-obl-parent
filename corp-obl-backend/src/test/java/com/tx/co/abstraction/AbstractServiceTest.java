package com.tx.co.abstraction;

import org.springframework.beans.factory.annotation.Autowired;

import com.tx.co.back_office.task.service.ITaskService;

/**
 * Base class for SERVICE testing.
 *
 * @author aazo
 */
public class AbstractServiceTest extends AbstractApiTest {

	private ITaskService taskService;

	public ITaskService getTaskService() {
		return taskService;
	}

	@Autowired
	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}
	
	
	
}
