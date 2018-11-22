package com.tx.co.back_office.tasktemplate.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.task.api.model.TaskResult;

/**
 * API model for returning task template office details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTemplateOfficeResult {

	private TaskTemplateResult taskTemplate;
	private OfficeResult office;
	private TaskResult task;
	private Boolean isSavingTaskTemplateTask = false;
	
	public TaskTemplateResult getTaskTemplate() {
		return taskTemplate;
	}
	public void setTaskTemplate(TaskTemplateResult taskTemplate) {
		this.taskTemplate = taskTemplate;
	}
	public OfficeResult getOffice() {
		return office;
	}
	public void setOffice(OfficeResult office) {
		this.office = office;
	}
	public TaskResult getTask() {
		return task;
	}
	public void setTask(TaskResult task) {
		this.task = task;
	}
	public Boolean getIsSavingTaskTemplateTask() {
		return isSavingTaskTemplateTask;
	}
	public void setIsSavingTaskTemplateTask(Boolean isSavingTaskTemplateTask) {
		this.isSavingTaskTemplateTask = isSavingTaskTemplateTask;
	}
	
	
}
