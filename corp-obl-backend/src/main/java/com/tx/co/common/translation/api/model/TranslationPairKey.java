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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		result = prime * result + ((tablename == null) ? 0 : tablename.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TranslationPairKey other = (TranslationPairKey) obj;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equalsIgnoreCase(other.lang))
			return false;
		if (tablename == null) {
			if (other.tablename != null)
				return false;
		} else if (!tablename.equalsIgnoreCase(other.tablename))
			return false;
		return true;
	}
	
	
	
}
