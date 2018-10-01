package com.tx.co.back_office.tasktemplateattachment.model.errors;

import java.io.Serializable;

public class HttpServiceError implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
