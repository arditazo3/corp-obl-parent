package com.tx.co.back_office.tasktemplateattachment.handler;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.tasktemplateattachment.exceptions.FileUploadException;
import com.tx.co.back_office.tasktemplateattachment.model.errors.ServiceError;
import com.tx.co.back_office.tasktemplateattachment.model.file.HttpFile;
import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.back_office.tasktemplateattachment.model.response.FileUploadResponse;

@Component
public class LocalStorageFileUploadHandler implements IFileUploadHandler {

	// set this to false to disable this job; set it it true by
    @Value("${file.upload.rootpath}")
    private String fileRootPath;

    @Override
    public FileUploadResponse handle(FileUploadRequest request) {

        // Early exit, if there is no Request:
        if(request == null) {
            throw new FileUploadException(new ServiceError("missingFile", "Missing File data"), String.format("Missing Parameter: request"));
        }

        // Get the HttpFile:
        HttpFile httpFile = request.getHttpFile();

        // Early exit, if the Request has no data assigned:
        if(httpFile == null) {
            throw new FileUploadException(new ServiceError("missingFile", "Missing File data"), String.format("Missing Parameter: request.httpFile"));
        }

        // We don't override existing files, create a new UUID File name:
        String targetFileName = UUID.randomUUID().toString();

        // Write it to Disk:
        internalWriteFile(httpFile.getStream(), targetFileName);

        return new FileUploadResponse(targetFileName);
    }

    private void internalWriteFile(InputStream stream, String fileName) {
        try {
            Files.copy(stream, Paths.get(fileRootPath, fileName));
        } catch(Exception e) {
            throw new FileUploadException(new ServiceError("storingFileError", "Error writing file"), String.format("Writing File '%s' failed", fileName), e);
        }
    }
}
