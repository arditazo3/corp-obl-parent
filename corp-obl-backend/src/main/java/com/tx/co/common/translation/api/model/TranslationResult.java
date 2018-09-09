package com.tx.co.common.translation.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning translation details.
 *
 * @author Ardit Azo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranslationResult {

	private Long idTranslation;
	private Long entityId;
	private String tablename;
	private String lang;
	private String description;
	
	public Long getIdTranslation() {
		return idTranslation;
	}
	public void setIdTranslation(Long idTranslation) {
		this.idTranslation = idTranslation;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}



}
