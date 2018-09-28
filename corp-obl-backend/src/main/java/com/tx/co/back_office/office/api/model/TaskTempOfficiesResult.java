package com.tx.co.back_office.office.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning office tasks details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTempOfficiesResult {

	private String descriptionTaskTemplate;
	private List<OfficeResult> officies;
	
	public String getDescriptionTaskTemplate() {
		return descriptionTaskTemplate;
	}
	public void setDescriptionTaskTemplate(String descriptionTaskTemplate) {
		this.descriptionTaskTemplate = descriptionTaskTemplate;
	}
	public List<OfficeResult> getOfficies() {
		return officies;
	}
	public void setOfficies(List<OfficeResult> officies) {
		this.officies = officies;
	}
	
	
}
