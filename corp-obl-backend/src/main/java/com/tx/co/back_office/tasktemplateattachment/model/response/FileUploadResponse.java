package com.tx.co.back_office.tasktemplateattachment.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUploadResponse {

    private final Object object;

    public FileUploadResponse(Object object) {
        this.object = object;
    }

    @JsonProperty("object")
	public Object getObject() {
		return object;
	}
}
