package com.tx.co.back_office.task.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateResult;

/**
 * API model for returning task details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TaskResult {

	private Long idTask;
	private Long idTaskTemplate;
	private TaskTemplateResult taskTemplate;
	private String recurrence;
	private String expirationType;
	private Integer day;
	private Integer daysOfNotice;
	private Integer frequenceOfNotice;
	private Integer daysBeforeShowExpiration;
	private List<TaskOfficeResult> taskOffices;
	
	public Long getIdTask() {
		return idTask;
	}
	public void setIdTask(Long idTask) {
		this.idTask = idTask;
	}
	public Long getIdTaskTemplate() {
		return idTaskTemplate;
	}
	public void setIdTaskTemplate(Long idTaskTemplate) {
		this.idTaskTemplate = idTaskTemplate;
	}
	public TaskTemplateResult getTaskTemplate() {
		return taskTemplate;
	}
	public void setTaskTemplate(TaskTemplateResult taskTemplate) {
		this.taskTemplate = taskTemplate;
	}
	public String getRecurrence() {
		return recurrence;
	}
	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}
	public String getExpirationType() {
		return expirationType;
	}
	public void setExpirationType(String expirationType) {
		this.expirationType = expirationType;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getDaysOfNotice() {
		return daysOfNotice;
	}
	public void setDaysOfNotice(Integer daysOfNotice) {
		this.daysOfNotice = daysOfNotice;
	}
	public Integer getFrequenceOfNotice() {
		return frequenceOfNotice;
	}
	public void setFrequenceOfNotice(Integer frequenceOfNotice) {
		this.frequenceOfNotice = frequenceOfNotice;
	}
	public Integer getDaysBeforeShowExpiration() {
		return daysBeforeShowExpiration;
	}
	public void setDaysBeforeShowExpiration(Integer daysBeforeShowExpiration) {
		this.daysBeforeShowExpiration = daysBeforeShowExpiration;
	}
	public List<TaskOfficeResult> getTaskOffices() {
		return taskOffices;
	}
	public void setTaskOffices(List<TaskOfficeResult> taskOffices) {
		this.taskOffices = taskOffices;
	}
	

	
}
