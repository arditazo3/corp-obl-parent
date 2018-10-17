package com.tx.co.front_end.expiration.service;

import java.util.Optional;

import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.front_end.expiration.domain.ExpirationActivityAttachment;

public interface IExpirationActivityAttachmentService {

	ExpirationActivityAttachment saveUpdateExpirationActivityAttachment(FileUploadRequest request);

	Optional<ExpirationActivityAttachment> findByIdExpirationActivityAttachment(Long idExpirationActivityAttachment);

	void deleteExpirationActivityAttachment(Long idExpirationActivityAttachment);
}
