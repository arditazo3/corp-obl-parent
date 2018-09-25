package com.tx.co.back_office.tasktemplateattachment.service;

import java.util.Optional;

import com.tx.co.back_office.tasktemplateattachment.model.TaskTemplateAttachment;
import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;

public interface ITaskTemplateAttachmentService {

	TaskTemplateAttachment saveUpdateTaskTemplateAttachment(FileUploadRequest request);
	
	Optional<TaskTemplateAttachment> findByIdTaskTemplateAttachment(Long idTaskTemplateAttachment);
	
	void deleteTaskTemplateAttachment(Long idTaskTemplateAttachment);
}
