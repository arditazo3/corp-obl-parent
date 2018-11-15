package com.tx.co.front_end.expiration.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.task.api.model.TaskResult;

/**
 * API model for returning task template expirations details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskOfficeExpirationsResult {

	private Long idTaskTemplate;
	private String description;
	private Integer totalExpirations;
	private Integer totalCompleted;
	private Integer totalArchived;
	private Integer totalExpired;
	private String colorDefined;
	private String expirationDate;
	private TaskResult task;
	private OfficeResult office;
	private List<ExpirationResult> expirations;
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
	public Integer getTotalExpired() {
		return totalExpired;
	}
	public void setTotalExpired(Integer totalExpired) {
		this.totalExpired = totalExpired;
	}
	public String getColorDefined() {
		return colorDefined;
	}
	public void setColorDefined(String colorDefined) {
		this.colorDefined = colorDefined;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
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
	public List<ExpirationResult> getExpirations() {
		return expirations;
	}
	public void setExpirations(List<ExpirationResult> expirations) {
		this.expirations = expirations;
	}
	public String getStatusExpirationOnChange() {
		return statusExpirationOnChange;
	}
	public void setStatusExpirationOnChange(String statusExpirationOnChange) {
		this.statusExpirationOnChange = statusExpirationOnChange;
	}
	
	
}
