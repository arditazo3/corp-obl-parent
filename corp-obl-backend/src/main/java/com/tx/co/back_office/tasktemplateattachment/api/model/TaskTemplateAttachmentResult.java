package com.tx.co.back_office.tasktemplateattachment.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning task template attachment details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTemplateAttachmentResult {

	private Long idTaskTemplateAttachment;
	private String fileName;
	private String fileType;
	private String filePath;
	private Long fileSize;
	
	public Long getIdTaskTemplateAttachment() {
		return idTaskTemplateAttachment;
	}
	public void setIdTaskTemplateAttachment(Long idTaskTemplateAttachment) {
		this.idTaskTemplateAttachment = idTaskTemplateAttachment;
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
