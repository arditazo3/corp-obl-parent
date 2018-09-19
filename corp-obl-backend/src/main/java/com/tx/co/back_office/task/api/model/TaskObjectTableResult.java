package com.tx.co.back_office.task.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.topic.api.model.TopicResult;
import com.tx.co.back_office.topic.domain.Topic;

/**
 * API model for returning task table details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskObjectTableResult {

	private String description;
	private List<CompanyResult> companies;
	private List<TopicResult> topics;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
