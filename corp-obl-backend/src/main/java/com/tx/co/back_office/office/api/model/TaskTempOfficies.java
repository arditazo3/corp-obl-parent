package com.tx.co.back_office.office.api.model;

import java.util.List;

import com.tx.co.back_office.office.domain.Office;

public class TaskTempOfficies {

	private String descriptionTaskTemplate;
	private List<Office> officies;
	
	public String getDescriptionTaskTemplate() {
		return descriptionTaskTemplate;
	}
	public void setDescriptionTaskTemplate(String descriptionTaskTemplate) {
		this.descriptionTaskTemplate = descriptionTaskTemplate;
	}
	public List<Office> getOfficies() {
		return officies;
	}
	public void setOfficies(List<Office> officies) {
		this.officies = officies;
	}
	
	
}
