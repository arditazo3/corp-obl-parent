package com.tx.co.common.translation.api.model;

import java.io.Serializable;

public class TranslationPairKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tablename;
	private String lang;
	
	public TranslationPairKey() {
		super();
	}

	public TranslationPairKey(String tablename, String lang) {
		super();
		this.tablename = tablename;
		this.lang = lang;
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
	
	
}
