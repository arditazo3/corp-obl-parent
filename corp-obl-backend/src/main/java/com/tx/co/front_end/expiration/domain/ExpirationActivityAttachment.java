package com.tx.co.front_end.expiration.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Domain model that represents a expiration activity attachment.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_expirationactivityattachment")
public class ExpirationActivityAttachment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long idExpirationActivityAttachment;
	
	@ManyToOne
    @JoinColumn(name = "expirationactivity_id")
    private ExpirationActivity expirationActivity;
	
	@Column(nullable = false, name="filename")
	private String fileName;
	
	@Column(nullable = false, name="filetype")
	private String fileType;
	
	@Column(nullable = false, name="filepath")
	private String filePath;
	
	@Column(nullable = false, name="filesize")
	private Long fileSize;
	
	@Column(nullable = false, name = "creationdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(nullable = false, name = "createdby")
	private String createdBy;

	@Column(nullable = false, name = "modificationdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationDate;

	@Column(nullable = false, name = "modifiedby")
	private String modifiedBy;

	public Long getIdExpirationActivityAttachment() {
		return idExpirationActivityAttachment;
	}

	public void setIdExpirationActivityAttachment(Long idExpirationActivityAttachment) {
		this.idExpirationActivityAttachment = idExpirationActivityAttachment;
	}

	public ExpirationActivity getExpirationActivity() {
		return expirationActivity;
	}

	public void setExpirationActivity(ExpirationActivity expirationActivity) {
		this.expirationActivity = expirationActivity;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
