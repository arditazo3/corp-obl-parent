package com.tx.co.back_office.tasktemplateattachment.model.request;

import com.tx.co.back_office.tasktemplateattachment.model.file.HttpFile;

public class FileUploadRequest {

    private final Long id;
    private final HttpFile httpFile;
    private final String incoming;

    public FileUploadRequest(Long id, HttpFile httpFile, String incoming) {
        this.id = id;
        this.httpFile = httpFile;
        this.incoming = incoming;
    }
    public Long getId() {
		return id;
	}
    public HttpFile getHttpFile() {
        return httpFile;
    }
	public String getIncoming() {
		return incoming;
	}
}
