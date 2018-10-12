package com.tx.co.back_office.task.service;

import java.util.List;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.model.TaskOfficeRelations;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface ITaskService {

	Task saveUpdateTask(Task task);
	
	List<Task> getTasks();
	
	TaskOffice saveUpdateTaskOffice(Task task, TaskOffice taskOffice);
	
	TaskOfficeRelations saveUpdateTaskOfficeRelation(TaskOfficeRelations taskOfficeRelation, TaskOffice taskOffice);
	
	TaskOffice getTaskOfficeByTaskTemplateAndOffice(TaskTemplate taskTemplate, Office office);
	
	Task getTasksByTaskTemplate(TaskTemplate taskTemplate);
	
	void deleteTask(Task task);
}
