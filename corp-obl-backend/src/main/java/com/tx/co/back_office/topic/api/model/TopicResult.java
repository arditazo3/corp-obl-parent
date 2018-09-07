package com.tx.co.back_office.topic.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.company.api.model.CompanyResult;

/**
 * API model for returning topic details.
 *
 * @author Ardit Azo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicResult {

	private Long idTopic;
	private String description;
	private CompanyResult company;
	
	public Long getIdTopic() {
		return idTopic;
	}
	public void setIdTopic(Long idTopic) {
		this.idTopic = idTopic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CompanyResult getCompany() {
		return company;
	}
	public void setCompany(CompanyResult company) {
		this.company = company;
	}


}
