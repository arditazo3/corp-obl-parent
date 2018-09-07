package com.tx.co.back_office.topic.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.api.model.CompanyTopicResult;

/**
 * API model for returning topic details.
 *
 * @author Ardit Azo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicResult {

	private Long idTopic;
	private String description;
	private List<CompanyTopicResult> companyTopicList;
	
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
	public List<CompanyTopicResult> getCompanyTopicList() {
		return companyTopicList;
	}
	public void setCompanyTopicList(List<CompanyTopicResult> companyTopicList) {
		this.companyTopicList = companyTopicList;
	}
}
