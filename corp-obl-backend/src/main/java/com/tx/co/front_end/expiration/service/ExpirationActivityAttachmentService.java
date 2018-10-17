package com.tx.co.front_end.expiration.service;

import java.util.Date;
import java.util.Optional;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;
import com.tx.co.front_end.expiration.domain.ExpirationActivityAttachment;
import com.tx.co.front_end.expiration.repository.ExpirationActivityAttachmentRepository;
import com.tx.co.front_end.expiration.repository.ExpirationActivityRepository;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;

/**
 * Service for {@link com.tx.co.front_end.expiration.domain.ExpirationActivityAttachment}s.
 *
 * @author aazo
 */
@Service
public class ExpirationActivityAttachmentService implements IExpirationActivityAttachmentService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(ExpirationActivityAttachmentService.class);

	private ExpirationActivityAttachmentRepository expirationActivityAttachmentRepository;
	private ExpirationActivityRepository expirationActivityRepository;

	@Autowired
	public void setExpirationActivityAttachmentRepository(
			ExpirationActivityAttachmentRepository expirationActivityAttachmentRepository) {
		this.expirationActivityAttachmentRepository = expirationActivityAttachmentRepository;
	}

	@Autowired
	public void setExpirationActivityRepository(ExpirationActivityRepository expirationActivityRepository) {
		this.expirationActivityRepository = expirationActivityRepository;
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public ExpirationActivityAttachment saveUpdateExpirationActivityAttachment(FileUploadRequest request) {

		logger.info("Creating the new TaskTemplateAttachment");

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		ExpirationActivityAttachment expirationActivityAttachment = new ExpirationActivityAttachment();

		expirationActivityAttachment.setCreationDate(new Date());
		expirationActivityAttachment.setCreatedBy(username);
		expirationActivityAttachment.setModificationDate(new Date());
		expirationActivityAttachment.setModifiedBy(username);

		ExpirationActivity expirationActivity = new ExpirationActivity();
		expirationActivity.setIdExpirationActivity(request.getId());

		expirationActivityAttachment.setExpirationActivity(expirationActivity);

		expirationActivityAttachment.setFileName(request.getHttpFile().getSubmittedFileName());
		expirationActivityAttachment.setFileType(request.getHttpFile().getFileType());
		expirationActivityAttachment.setFilePath(request.getHttpFile().getFilePath());
		expirationActivityAttachment.setFileSize(request.getHttpFile().getSize());

		expirationActivityAttachment = expirationActivityAttachmentRepository.save(expirationActivityAttachment);

		return expirationActivityAttachment;
	}

	@Override
	public Optional<ExpirationActivityAttachment> findByIdExpirationActivityAttachment(
			Long idExpirationActivityAttachment) {
		return expirationActivityAttachmentRepository.findById(idExpirationActivityAttachment);
	}

	@Override
	public void deleteExpirationActivityAttachment(Long idExpirationActivityAttachment) {
		
		Optional<ExpirationActivityAttachment> expirationActivityAttachmentFromDB = 
				findByIdExpirationActivityAttachment(idExpirationActivityAttachment);
		
		if(expirationActivityAttachmentFromDB.isPresent()) {
			ExpirationActivityAttachment expirationActivityAttachment = expirationActivityAttachmentFromDB.get();

			ExpirationActivity expirationActivity = expirationActivityAttachment.getExpirationActivity();
			
			expirationActivity.getExpirationActivityAttachments().remove(expirationActivityAttachment);
			
			logger.info("Deleted the ExpirationActivityAttachment with id: " + idExpirationActivityAttachment);
			
			expirationActivityRepository.save(expirationActivity);
		}
	}

}
