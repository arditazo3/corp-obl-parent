package com.tx.co.back_office.tasktemplate.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.topic.api.model.TopicResult;

/**
 * API model for returning task template details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTemplateResult {
	
	private Long idTaskTemplate;
	private TopicResult topic;
	private String description;
	private String recurrence;
	private String expirationType;
	private Integer day;
	private Integer daysOfNotice;
	private Integer frequenceOfNotice;
	private Integer daysBeforeShowExpiration;
	private Integer expirationClosableBy;
	
	public Long getIdTaskTemplate() {
		return idTaskTemplate;
	}
	public void setIdTaskTemplate(Long idTaskTemplate) {
		this.idTaskTemplate = idTaskTemplate;
	}
	public TopicResult getTopic() {
		return topic;
	}
	public void setTopic(TopicResult topic) {
		this.topic = topic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRecurrence() {
		return recurrence;
	}
	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}
	public String getExpirationType() {
		return expirationType;
	}
	public void setExpirationType(String expirationType) {
		this.expirationType = expirationType;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getDaysOfNotice() {
		return daysOfNotice;
	}
	public void setDaysOfNotice(Integer daysOfNotice) {
		this.daysOfNotice = daysOfNotice;
	}
	public Integer getFrequenceOfNotice() {
		return frequenceOfNotice;
	}
	public void setFrequenceOfNotice(Integer frequenceOfNotice) {
		this.frequenceOfNotice = frequenceOfNotice;
	}
	public Integer getDaysBeforeShowExpiration() {
		return daysBeforeShowExpiration;
	}
	public void setDaysBeforeShowExpiration(Integer daysBeforeShowExpiration) {
		this.daysBeforeShowExpiration = daysBeforeShowExpiration;
	}
	public Integer getExpirationClosableBy() {
		return expirationClosableBy;
	}
	public void setExpirationClosableBy(Integer expirationClosableBy) {
		this.expirationClosableBy = expirationClosableBy;
	}
	
	
}
