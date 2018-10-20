package com.tx.co.front_end.expiration.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning task template expirations details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTemplateExpirationsResult {

	private Long idTaskTemplate;
	private String description;
	private Integer totalExpirations;
	private Integer totalCompleted;
	private String colorDefined;
	private String expirationDate;
	private List<TaskExpirationsResult> taskExpirations;
	
	public Long getIdTaskTemplate() {
		return idTaskTemplate;
	}
	public void setIdTaskTemplate(Long idTaskTemplate) {
		this.idTaskTemplate = idTaskTemplate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getTotalExpirations() {
		return totalExpirations;
	}
	public void setTotalExpirations(Integer totalExpirations) {
		this.totalExpirations = totalExpirations;
	}
	public Integer getTotalCompleted() {
		return totalCompleted;
	}
	public void setTotalCompleted(Integer totalCompleted) {
		this.totalCompleted = totalCompleted;
	}
	public String getColorDefined() {
		return colorDefined;
	}
	public void setColorDefined(String colorDefined) {
		this.colorDefined = colorDefined;
	}
	public List<TaskExpirationsResult> getTaskExpirations() {
		return taskExpirations;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public void setTaskExpirations(List<TaskExpirationsResult> taskExpirations) {
		this.taskExpirations = taskExpirations;
	}
}
