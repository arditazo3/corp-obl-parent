package com.tx.co.common.translation.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Domain model that represents a translation.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_translations")
public class Translation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
	private Long idTranslation;
	
	@Column(nullable = false, name = "entity_id")
	private Long entityId;
	
	@Column(nullable = false)
	private String tablename;
	
	@Column(nullable = false)
	private String lang;
	
	@Column(nullable = false)
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
