package com.tx.co.back_office.tasktemplateattachment.service;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplate.repository.TaskTemplateRepository;
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

	private static final Logger logger = LogManager.getLogger(TaskTemplateAttachmentService.class);

	private TaskTemplateAttachmentRepository taskTemplateAttachmentRepository;
	private TaskTemplateRepository taskTemplateRepository;

	@Autowired
	public void setTaskTemplateAttachmentRepository(TaskTemplateAttachmentRepository taskTemplateAttachmentRepository) {
		this.taskTemplateAttachmentRepository = taskTemplateAttachmentRepository;
	}

	@Autowired
	public void setTaskTemplateRepository(TaskTemplateRepository taskTemplateRepository) {
		this.taskTemplateRepository = taskTemplateRepository;
	}


	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public TaskTemplateAttachment saveUpdateTaskTemplateAttachment(FileUploadRequest request) {

		logger.info("Creating the new TaskTemplateAttachment");
		
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
		taskTemplateAttachment.setFileType(request.getHttpFile().getFileType());
		taskTemplateAttachment.setFilePath(request.getHttpFile().getFilePath());
		taskTemplateAttachment.setFileSize(request.getHttpFile().getSize());

		taskTemplateAttachment = taskTemplateAttachmentRepository.save(taskTemplateAttachment);

		updateTaskTemplateAttachmentCache(taskTemplateAttachment, false);

		return taskTemplateAttachment;
	}

	@Override
	public Optional<TaskTemplateAttachment> findByIdTaskTemplateAttachment(Long idTaskTemplateAttachment) {
		return taskTemplateAttachmentRepository.findById(idTaskTemplateAttachment);
	}

	@Override
	@Transactional
	public void deleteTaskTemplateAttachment(Long idTaskTemplateAttachment) {

		Optional<TaskTemplateAttachment> taskTemplateFromDB = findByIdTaskTemplateAttachment(idTaskTemplateAttachment);
		if(taskTemplateFromDB.isPresent()) {
			TaskTemplateAttachment taskTemplateAttachment = taskTemplateFromDB.get();

			TaskTemplate taskTemplate = taskTemplateAttachment.getTaskTemplate();
			
			taskTemplate.getTaskTemplateAttachments().remove(taskTemplateAttachment);
			
			logger.info("Deleted the TaskTemplateAttachment with id: " + idTaskTemplateAttachment);
			
			taskTemplateRepository.save(taskTemplate);
		}
	}

}
