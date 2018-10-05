package com.tx.co.common.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model that represents an string.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StringResult {

	String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
