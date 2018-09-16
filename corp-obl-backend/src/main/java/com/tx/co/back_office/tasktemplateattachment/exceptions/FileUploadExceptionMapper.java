package com.tx.co.back_office.tasktemplateattachment.exceptions;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tx.co.back_office.tasktemplateattachment.model.errors.HttpServiceError;

public class FileUploadExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<FileUploadException> {

    private static final Logger logger = LogManager.getLogger(FileUploadExceptionMapper.class);

    @Override
    public Response toResponse(FileUploadException fileUploadException) {

        if(logger.isErrorEnabled()) {
            logger.error("An error occured", fileUploadException);
        }

        HttpServiceError httpServiceError = fileUploadException.getHttpServiceError();

        return Response
                .status(httpServiceError.getHttpStatusCode())
                .entity(httpServiceError.getServiceError())
                .build();
    }
}