package com.tx.co.back_office.tasktemplate.service;

import java.util.List;
import java.util.Optional;

import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface ITaskTemplateService {

	List<Task> getTasksForTable();
	
	TaskTemplate saveUpdateTaskTemplate(TaskTemplate taskTemplate);
	
	Optional<TaskTemplate> findByIdTaskTemplate(Long idTaskTemplate);
}
