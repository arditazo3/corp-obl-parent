package com.tx.co.back_office.tasktemplateattachment.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplateattachment.model.TaskTemplateAttachment;
import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.back_office.tasktemplateattachment.repository.TaskTemplateAttachmentRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;

/**
 * Service for {@link com.tx.co.back_office.tasktemplateattachment.model.TaskTemplateAttachment}s.
 *
 * @author aazo
 */
@Service
public class TaskTemplateAttachmentService extends UpdateCacheData implements ITaskTemplateAttachmentService, IUserManagementDetails {

	private TaskTemplateAttachmentRepository taskTemplateAttachmentRepository;

	@Autowired
	public void setTaskTemplateAttachmentRepository(TaskTemplateAttachmentRepository taskTemplateAttachmentRepository) {
		this.taskTemplateAttachmentRepository = taskTemplateAttachmentRepository;
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public TaskTemplateAttachment saveUpdateTaskTemplateAttachment(FileUploadRequest request) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskTemplateAttachment taskTemplateAttachment = new TaskTemplateAttachment();

		taskTemplateAttachment.setCreationDate(new Date());
		taskTemplateAttachment.setCreatedBy(username);
		taskTemplateAttachment.setModificationDate(new Date());
		taskTemplateAttachment.setModifiedBy(username);

		TaskTemplate taskTemplate = new TaskTemplate();
		taskTemplate.setIdTaskTemplate(request.getId());

		taskTemplateAttachment.setTaskTemplate(taskTemplate);
		
		taskTemplateAttachment.setFileName(request.getHttpFile().getSubmittedFileName());
		
		String[] splitFileName = request.getHttpFile().getSubmittedFileName().split("\\.");
		if(!isEmpty(splitFileName)) {
			taskTemplateAttachment.setFileType(splitFileName[splitFileName.length-1]);
		} else {
			taskTemplateAttachment.setFileType(request.getHttpFile().getFileType());
		}
		taskTemplateAttachment.setFilePath(request.getHttpFile().getFilePath());

		taskTemplateAttachment = taskTemplateAttachmentRepository.save(taskTemplateAttachment);

		updateTaskTemplateAttachmentCache(taskTemplateAttachment, false);

		return taskTemplateAttachment;
	}

	@Override
	public Optional<TaskTemplateAttachment> findByIdTaskTemplateAttachment(Long idTaskTemplateAttachment) {
		return taskTemplateAttachmentRepository.findById(idTaskTemplateAttachment);
	}

}
