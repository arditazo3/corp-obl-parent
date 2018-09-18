package com.tx.co.back_office.tasktemplateattachment.model.request;

import com.tx.co.back_office.tasktemplateattachment.model.file.HttpFile;

public class FileUploadRequest {

    private final Long id;
    private final HttpFile httpFile;

    public FileUploadRequest(Long id, HttpFile httpFile) {
        this.id = id;
        this.httpFile = httpFile;
    }
    public Long getId() {
		return id;
	}
    public HttpFile getHttpFile() {
        return httpFile;
    }
}
