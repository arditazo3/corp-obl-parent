package com.tx.co.front_end.expiration.api.model;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning expiration activity details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpirationActivityResult {

	private Long idExpirationActivity;
    private ExpirationResult expiration;
    
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
    
    
}
