package com.tx.co.back_office.tasktemplateattachment.handler;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.tasktemplateattachment.exceptions.FileUploadException;
import com.tx.co.back_office.tasktemplateattachment.model.TaskTemplateAttachment;
import com.tx.co.back_office.tasktemplateattachment.model.errors.ServiceError;
import com.tx.co.back_office.tasktemplateattachment.model.file.HttpFile;
import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.back_office.tasktemplateattachment.model.response.FileUploadResponse;
import com.tx.co.back_office.tasktemplateattachment.service.ITaskTemplateAttachmentService;

@Component
public class LocalStorageFileUploadHandler implements IFileUploadHandler {

	// set this to false to disable this job; set it it true by
    @Value("${file.upload.rootpath}")
    private String fileRootPath;
    
    private ITaskTemplateAttachmentService taskTemplateAttachmentService;
    
    @Autowired    
    public void setTaskTemplateAttachmentService(ITaskTemplateAttachmentService taskTemplateAttachmentService) {
		this.taskTemplateAttachmentService = taskTemplateAttachmentService;
	}

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

        // We don't override existing files
        String targetFileName = httpFile.getSubmittedFileName();

        // Write it to Disk:
        internalWriteFile(httpFile.getStream(), request, targetFileName);
        
        TaskTemplateAttachment taskTemplateAttachment = taskTemplateAttachmentService.saveUpdateTaskTemplateAttachment(request);

        return new FileUploadResponse(taskTemplateAttachment);
    }

    private void internalWriteFile(InputStream stream, FileUploadRequest request, String targetFileName) {
        try {
        	Calendar now = Calendar.getInstance();
        	int year = now.get(Calendar.YEAR);
        	int month = now.get(Calendar.MONTH);
        	int day = now.get(Calendar.DAY_OF_MONTH);
        	String yearInString = String.valueOf(year);
        	String monthInString = String.valueOf(month);
        	String dayInString = String.valueOf(day);
        	String idString = request.getId().toString();
        	
        	File currentDirFile = new File(".");
        	String helper = currentDirFile.getAbsolutePath();
        	String currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());
        	
        	String fullFilePath = currentDir + File.separator + fileRootPath + File.separator + yearInString + File.separator + monthInString + File.separator + dayInString;
        	targetFileName = fullFilePath + File.separator + idString.concat("_").concat(targetFileName);
        	request.getHttpFile().setFilePath(targetFileName);
        	
        	File file = new File(targetFileName);
        	file.getParentFile().mkdirs();
        	
            Files.copy(stream, Paths.get(targetFileName));
            
        } catch(Exception e) {
            throw new FileUploadException(new ServiceError("storingFileError", "Error writing file"), String.format("Writing File '%s' failed", targetFileName), e);
        }
    }
}
