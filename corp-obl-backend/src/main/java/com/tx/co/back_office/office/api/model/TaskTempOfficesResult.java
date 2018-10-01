package com.tx.co.back_office.office.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning office tasks details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTempOfficesResult {

	private String descriptionTaskTemplate;
	private List<OfficeResult> offices;
	
	public String getDescriptionTaskTemplate() {
		return descriptionTaskTemplate;
	}
	public void setDescriptionTaskTemplate(String descriptionTaskTemplate) {
		this.descriptionTaskTemplate = descriptionTaskTemplate;
	}
	public List<OfficeResult> getOffices() {
		return offices;
	}
	public void setOffices(List<OfficeResult> offices) {
		this.offices = offices;
	}
	
	
}
