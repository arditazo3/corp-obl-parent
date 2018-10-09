package com.tx.co.back_office.task.service;

import java.util.List;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.model.TaskOfficeRelations;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.topic.domain.Topic;

public interface ITaskService {

	Task saveUpdateTask(Task task);
	
	List<Task> getTasks();
	
	List<Task> getTasksByDescriptionOrCompaniesOrTopics(String description, List<Company> companies, List<Topic> topics);
	
	TaskOffice saveUpdateTaskOffice(Task task, TaskOffice taskOffice);
	
	TaskOfficeRelations saveUpdateTaskOfficeRelation(TaskOfficeRelations taskOfficeRelation, TaskOffice taskOffice);
	
	TaskOffice getTaskOfficeByTaskTemplateAndOffice(TaskTemplate taskTemplate, Office office);
	
	void deleteTask(Task task);
}
