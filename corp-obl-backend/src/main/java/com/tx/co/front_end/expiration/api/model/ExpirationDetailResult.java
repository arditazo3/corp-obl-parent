package com.tx.co.front_end.expiration.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.front_end.expiration.enums.StatusExpirationEnum;

/**
 * API model for returning expiration details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpirationDetailResult {

	private String colorDefined;
	private String expirationDescriptionDate;
	private StatusExpirationEnum statusExpiration;
	
	public String getColorDefined() {
		return colorDefined;
	}
	public void setColorDefined(String colorDefined) {
		this.colorDefined = colorDefined;
	}
	public String getExpirationDescriptionDate() {
		return expirationDescriptionDate;
	}
	public void setExpirationDescriptionDate(String expirationDescriptionDate) {
		this.expirationDescriptionDate = expirationDescriptionDate;
	}
	public StatusExpirationEnum getStatusExpiration() {
		return statusExpiration;
	}
	public void setStatusExpiration(StatusExpirationEnum statusExpiration) {
		this.statusExpiration = statusExpiration;
	}
	
	
}
