package com.tx.co.front_end.expiration.api.model;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.task.api.model.TaskResult;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

/**
 * API model for returning expiration details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpirationResult {

	private Long idExpiration;
	private TaskTemplate taskTemplate;
	private TaskResult task;
	private OfficeResult office;
	private String expirationClosableBy;
	private String username;
	private Date expirationDate;
	private Date completed;
	private Date approved;
	private Date registered;

	public Long getIdExpiration() {
		return idExpiration;
	}

	public void setIdExpiration(Long idExpiration) {
		this.idExpiration = idExpiration;
	}

	public TaskTemplate getTaskTemplate() {
		return taskTemplate;
	}

	public void setTaskTemplate(TaskTemplate taskTemplate) {
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

	public String getExpirationClosableBy() {
		return expirationClosableBy;
	}

	public void setExpirationClosableBy(String expirationClosableBy) {
		this.expirationClosableBy = expirationClosableBy;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getCompleted() {
		return completed;
	}

	public void setCompleted(Date completed) {
		this.completed = completed;
	}

	public Date getApproved() {
		return approved;
	}

	public void setApproved(Date approved) {
		this.approved = approved;
	}

	public Date getRegistered() {
		return registered;
	}

	public void setRegistered(Date registered) {
		this.registered = registered;
	}
	
	
}
