package com.tx.co.front_end.expiration.api.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

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
    private String descriptionLastActivity;
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
	public String getDescriptionLastActivity() {
		return descriptionLastActivity;
	}
	public void setDescriptionLastActivity(String descriptionLastActivity) {
		this.descriptionLastActivity = descriptionLastActivity;
	}
	public List<ExpirationActivityAttachmentResult> getExpirationActivityAttachments() {
		return expirationActivityAttachments;
	}
	public void setExpirationActivityAttachments(List<ExpirationActivityAttachmentResult> expirationActivityAttachments) {
		this.expirationActivityAttachments = expirationActivityAttachments;
	}
}
