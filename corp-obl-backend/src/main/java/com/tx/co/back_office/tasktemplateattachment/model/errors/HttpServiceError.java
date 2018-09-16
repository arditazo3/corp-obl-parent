package com.tx.co.back_office.tasktemplateattachment.model.errors;

public class HttpServiceError {

    private final int httpStatusCode;

    private final ServiceError serviceError;

    public HttpServiceError(int httpStatusCode, ServiceError serviceError) {
        this.httpStatusCode = httpStatusCode;
        this.serviceError = serviceError;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public ServiceError getServiceError() {
        return serviceError;
    }
}
