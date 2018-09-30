package com.tx.co.back_office.task.api.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateResult;

/**
 * API model for returning task office details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskOfficeResult {

	private Long idTaskOffice;

	private TaskTemplateResult taskTemplate;

	private TaskResult task;

	private OfficeResult office;

	List<TaskOfficeRelationsResult> taskOfficeRelations;

	private Date startDate;

	private Date endDate;

	public Long getIdTaskOffice() {
		return idTaskOffice;
	}

	public void setIdTaskOffice(Long idTaskOffice) {
		this.idTaskOffice = idTaskOffice;
	}

	public TaskTemplateResult getTaskTemplate() {
		return taskTemplate;
	}

	public void setTaskTemplate(TaskTemplateResult taskTemplate) {
		this.taskTemplate = taskTemplate;
	}

	public TaskResult getTask() {
		return task;
	}

	public void setTask(TaskResult task) {
		this.task = task;
	}

	public OfficeResult getOffice() {
		return office;
	}

	public void setOffice(OfficeResult office) {
		this.office = office;
	}

	public List<TaskOfficeRelationsResult> getTaskOfficeRelations() {
		return taskOfficeRelations;
	}

	public void setTaskOfficeRelations(List<TaskOfficeRelationsResult> taskOfficeRelations) {
		this.taskOfficeRelations = taskOfficeRelations;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
