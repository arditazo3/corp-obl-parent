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
	private Integer totalTasks;
	private Integer totalCompleted;
	private List<TaskExpirationsResult> tasks;
	
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
	public Integer getTotalTasks() {
		return totalTasks;
	}
	public void setTotalTasks(Integer totalTasks) {
		this.totalTasks = totalTasks;
	}
	public Integer getTotalCompleted() {
		return totalCompleted;
	}
	public void setTotalCompleted(Integer totalCompleted) {
		this.totalCompleted = totalCompleted;
	}
	public List<TaskExpirationsResult> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskExpirationsResult> tasks) {
		this.tasks = tasks;
	}
	
	
	
}
