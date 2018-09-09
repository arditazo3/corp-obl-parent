package com.tx.co.back_office.company.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.topic.api.model.TopicResult;

/**
* API model for returning company topic details.
*
* @author Ardit Azo
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyTopicResult {

	private Long idCompanyTopic;
	private CompanyResult company;
	private TopicResult topic;
	
	public Long getIdCompanyTopic() {
		return idCompanyTopic;
	}
	public void setIdCompanyTopic(Long idCompanyTopic) {
		this.idCompanyTopic = idCompanyTopic;
	}
	public CompanyResult getCompany() {
		return company;
	}
	public void setCompany(CompanyResult company) {
		this.company = company;
	}
	public TopicResult getTopic() {
		return topic;
	}
	public void setTopic(TopicResult topic) {
		this.topic = topic;
	}
	
	
}
