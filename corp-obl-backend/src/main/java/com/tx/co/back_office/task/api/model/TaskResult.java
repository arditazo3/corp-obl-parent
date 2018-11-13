package com.tx.co.back_office.task.api.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateResult;
import com.tx.co.common.api.model.DescriptionLangResult;

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
	private Integer counterCompany;
	private OfficeResult office;
	private Boolean excludeOffice = false; 
	private List<DescriptionLangResult> descriptionLangList = new ArrayList<>();
	
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
	public Integer getCounterCompany() {
		return counterCompany;
	}
	public void setCounterCompany(Integer counterCompany) {
		this.counterCompany = counterCompany;
	}
	public OfficeResult getOffice() {
		return office;
	}
	public void setOffice(OfficeResult office) {
		this.office = office;
	}
	public Boolean getExcludeOffice() {
		return excludeOffice;
	}
	public void setExcludeOffice(Boolean excludeOffice) {
		this.excludeOffice = excludeOffice;
	}
	public List<DescriptionLangResult> getDescriptionLangList() {
		return descriptionLangList;
	}
	public void setDescriptionLangList(List<DescriptionLangResult> descriptionLangList) {
		this.descriptionLangList = descriptionLangList;
	}
}
