package com.tx.co.back_office.topic.api.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.topic.domain.Topic;

/**
 * API model for returning topic consultant details.
 *
 * @author Ardit Azo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicConsultantResult {

	private Long idTopicConsultant;
	private Topic topic;
	private CompanyConsultant companyConsultant;
	
	public Long getIdTopicConsultant() {
		return idTopicConsultant;
	}
	public void setIdTopicConsultant(Long idTopicConsultant) {
		this.idTopicConsultant = idTopicConsultant;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public CompanyConsultant getCompanyConsultant() {
		return companyConsultant;
	}
	public void setCompanyConsultant(CompanyConsultant companyConsultant) {
		this.companyConsultant = companyConsultant;
	}
}
