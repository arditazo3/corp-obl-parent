package com.tx.co.back_office.tasktemplateattachment.handler;

import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.back_office.tasktemplateattachment.model.response.FileUploadResponse;

public interface IFileUploadHandler {

    FileUploadResponse handle(FileUploadRequest request);

}