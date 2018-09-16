package com.tx.co.back_office.tasktemplateattachment.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUploadResponse {

    private final String identifier;

    public FileUploadResponse(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }
}
