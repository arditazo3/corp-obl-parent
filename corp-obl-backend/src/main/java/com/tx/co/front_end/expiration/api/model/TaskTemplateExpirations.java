package com.tx.co.front_end.expiration.api.model;

import java.util.Set;

import com.tx.co.back_office.task.model.Task;

/**
 * POJO class to convert it as Result class
 * */
public class TaskTemplateExpirations {

	private Long idTaskTemplate;
	private String description;
	private Integer totalExpirations;
	private Integer totalCompleted;
	private Set<Task> tasks;
	
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
	public Set<Task> getTasks() {
		return tasks;
	}
	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	
	
}
