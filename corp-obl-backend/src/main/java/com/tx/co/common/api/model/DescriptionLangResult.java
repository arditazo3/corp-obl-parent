package com.tx.co.common.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model that represents an description lang.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DescriptionLangResult {

	private String description;
	private String lang;
	
	public DescriptionLangResult(String description, String lang) {
		super();
		this.description = description;
		this.lang = lang;
	}
	public DescriptionLangResult() {
		super();
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	
}
