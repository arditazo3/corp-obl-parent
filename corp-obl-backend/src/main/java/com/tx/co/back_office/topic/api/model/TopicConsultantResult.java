package com.tx.co.back_office.topic.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.company.api.model.CompanyConsultantResult;

/**
 * API model for returning topic consultant details.
 *
 * @author Ardit Azo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicConsultantResult {

	private TopicResult topic;
	private CompanyConsultantResult consultant;

	public TopicResult getTopic() {
		return topic;
	}
	public void setTopic(TopicResult topic) {
		this.topic = topic;
	}
	public CompanyConsultantResult getConsultant() {
		return consultant;
	}
	public void setConsultant(CompanyConsultantResult consultant) {
		this.consultant = consultant;
	}
	
}
