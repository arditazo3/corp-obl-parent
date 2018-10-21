package com.tx.co.front_end.expiration.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning expiration activity attachment details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpirationActivityAttachmentResult {

	private Long idExpirationActivityAttachment;
	private String fileName;
	private String fileType;
	private String filePath;
	private Long fileSize;

	public Long getIdExpirationActivityAttachment() {
		return idExpirationActivityAttachment;
	}

	public void setIdExpirationActivityAttachment(Long idExpirationActivityAttachment) {
		this.idExpirationActivityAttachment = idExpirationActivityAttachment;
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


}
