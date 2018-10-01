package com.tx.co.back_office.office.api.model;

import java.util.List;

import com.tx.co.back_office.office.domain.Office;

public class TaskTempOffices {

	private String descriptionTaskTemplate;
	private List<Office> offices;
	
	public String getDescriptionTaskTemplate() {
		return descriptionTaskTemplate;
	}
	public void setDescriptionTaskTemplate(String descriptionTaskTemplate) {
		this.descriptionTaskTemplate = descriptionTaskTemplate;
	}
	public List<Office> getOffices() {
		return offices;
	}
	public void setOffices(List<Office> offices) {
		this.offices = offices;
	}
	
	
}
