package com.tx.co.front_end.expiration.api.model;

import java.util.HashSet;
import java.util.Set;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.front_end.expiration.domain.Expiration;

/**
 * POJO class to convert it as Result class
 * */
public class TaskOfficeExpirations {

	private Long idTaskTemplate;
	private String description;
	private Task task;
	private Office office;
	private Integer totalExpirations;
	private Integer totalCompleted;
	private Integer totalArchived;
	private String expirationDate;
	private Set<Expiration> expirations = new HashSet<>();
	private String statusExpirationOnChange;
	
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
	public Integer getTotalArchived() {
		return totalArchived;
	}
	public void setTotalArchived(Integer totalArchived) {
		this.totalArchived = totalArchived;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public Set<Expiration> getExpirations() {
		return expirations;
	}
	public void setExpirations(Set<Expiration> expirations) {
		this.expirations = expirations;
	}
	public String getStatusExpirationOnChange() {
		return statusExpirationOnChange;
	}
	public void setStatusExpirationOnChange(String statusExpirationOnChange) {
		this.statusExpirationOnChange = statusExpirationOnChange;
	}
}
