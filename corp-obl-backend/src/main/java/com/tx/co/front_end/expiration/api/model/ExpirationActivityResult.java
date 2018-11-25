package com.tx.co.front_end.expiration.api.model;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.common.api.model.DescriptionLangResult;

/**
 * API model for returning expiration activity details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ExpirationActivityResult {

	private Long idExpirationActivity;
    private ExpirationResult expiration;
    private String body;
    private List<DescriptionLangResult> descriptionActivity = new ArrayList<>();
    private List<ExpirationActivityAttachmentResult> expirationActivityAttachments;
    
	public Long getIdExpirationActivity() {
		return idExpirationActivity;
	}
	public void setIdExpirationActivity(Long idExpirationActivity) {
		this.idExpirationActivity = idExpirationActivity;
	}
	public ExpirationResult getExpiration() {
		return expiration;
	}
	public void setExpiration(ExpirationResult expiration) {
		this.expiration = expiration;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public List<DescriptionLangResult> getDescriptionActivity() {
		return descriptionActivity;
	}
	public void setDescriptionActivity(List<DescriptionLangResult> descriptionActivity) {
		this.descriptionActivity = descriptionActivity;
	}
	public List<ExpirationActivityAttachmentResult> getExpirationActivityAttachments() {
		return expirationActivityAttachments;
	}
	public void setExpirationActivityAttachments(List<ExpirationActivityAttachmentResult> expirationActivityAttachments) {
		this.expirationActivityAttachments = expirationActivityAttachments;
	}
}
