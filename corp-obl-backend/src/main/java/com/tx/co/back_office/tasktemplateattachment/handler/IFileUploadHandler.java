package com.tx.co.back_office.tasktemplateattachment.handler;

import com.tx.co.back_office.tasktemplateattachment.api.model.TaskTemplateAttachmentResult;
import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.back_office.tasktemplateattachment.model.response.FileUploadResponse;
import com.tx.co.front_end.expiration.api.model.ExpirationActivityAttachmentResult;

public interface IFileUploadHandler {

    FileUploadResponse handle(FileUploadRequest request);
    
    void deleteFile(TaskTemplateAttachmentResult taskTemplateAttachmentResult);
    
    void deleteFileExp(ExpirationActivityAttachmentResult expirationActivityAttachmentResult);

}