package com.tx.co.common.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model that represents an string.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class StringResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String result;

	public StringResult() {
		super();
	}

	public StringResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
