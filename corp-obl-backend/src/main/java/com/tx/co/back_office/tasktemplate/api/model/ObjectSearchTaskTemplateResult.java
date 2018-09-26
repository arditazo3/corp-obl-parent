package com.tx.co.back_office.tasktemplate.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.topic.api.model.TopicResult;

/**
 * API model for returning task object search task template details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectSearchTaskTemplateResult {

	private String descriptionTaskTemplate;
	List<CompanyResult> companies;
	List<TopicResult> topics;
	
	public String getDescriptionTaskTemplate() {
		return descriptionTaskTemplate;
	}
	public void setDescriptionTaskTemplate(String descriptionTaskTemplate) {
		this.descriptionTaskTemplate = descriptionTaskTemplate;
	}
	public List<CompanyResult> getCompanies() {
		return companies;
	}
	public void setCompanies(List<CompanyResult> companies) {
		this.companies = companies;
	}
	public List<TopicResult> getTopics() {
		return topics;
	}
	public void setTopics(List<TopicResult> topics) {
		this.topics = topics;
	}
	
	
}
