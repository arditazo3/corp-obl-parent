package com.tx.co.back_office.tasktemplate.api.model;

import java.util.List;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.topic.domain.Topic;

public class ObjectSearchTaskTemplate {

	private String descriptionTaskTemplate;
	List<Company> companies;
	List<Topic> topics;
	
	public String getDescriptionTaskTemplate() {
		return descriptionTaskTemplate;
	}
	public void setDescriptionTaskTemplate(String descriptionTaskTemplate) {
		this.descriptionTaskTemplate = descriptionTaskTemplate;
	}
	public List<Company> getCompanies() {
		return companies;
	}
	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	public List<Topic> getTopics() {
		return topics;
	}
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	
	
}
