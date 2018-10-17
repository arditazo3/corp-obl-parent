package com.tx.co.back_office.tasktemplateattachment.handler;

import static com.tx.co.common.constants.ApiConstants.*;
import static com.tx.co.common.constants.AppConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.tasktemplateattachment.api.model.TaskTemplateAttachmentResult;
import com.tx.co.back_office.tasktemplateattachment.exceptions.FileUploadException;
import com.tx.co.back_office.tasktemplateattachment.model.TaskTemplateAttachment;
import com.tx.co.back_office.tasktemplateattachment.model.errors.ServiceError;
import com.tx.co.back_office.tasktemplateattachment.model.file.HttpFile;
import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.back_office.tasktemplateattachment.model.response.FileUploadResponse;
import com.tx.co.back_office.tasktemplateattachment.service.ITaskTemplateAttachmentService;
import com.tx.co.front_end.expiration.domain.ExpirationActivityAttachment;
import com.tx.co.front_end.expiration.service.IExpirationActivityAttachmentService;
import com.tx.co.security.exception.GeneralException;

@Component
public class LocalStorageFileUploadHandler implements IFileUploadHandler {

	private static final Logger logger = LogManager.getLogger(LocalStorageFileUploadHandler.class);

	// set this to false to disable this job; set it it true by
	@Value("${file.upload.rootpath}")
	private String fileRootPath;

	private ITaskTemplateAttachmentService taskTemplateAttachmentService;
	private IExpirationActivityAttachmentService expirationActivityAttachmentService;

	@Autowired    
	public void setTaskTemplateAttachmentService(ITaskTemplateAttachmentService taskTemplateAttachmentService) {
		this.taskTemplateAttachmentService = taskTemplateAttachmentService;
	}
	
	@Autowired    
	public void setExpirationActivityAttachmentService(
			IExpirationActivityAttachmentService expirationActivityAttachmentService) {
		this.expirationActivityAttachmentService = expirationActivityAttachmentService;
	}

	@Override
	public FileUploadResponse handle(FileUploadRequest request) {

		// Early exit, if there is no Request:
		if(request == null) {
			throw new FileUploadException(new ServiceError("missingFile", "Missing File data"), "Missing Parameter: request");
		}

		// Get the HttpFile:
		HttpFile httpFile = request.getHttpFile();

		// Early exit, if the Request has no data assigned:
		if(httpFile == null) {
			throw new FileUploadException(new ServiceError("missingFile", "Missing File data"), "Missing Parameter: request.httpFile");
		}

		// We don't override existing files
		String targetFileName = httpFile.getSubmittedFileName();

		// Write it to Disk:
		internalWriteFile(httpFile.getStream(), request, targetFileName);

		setSizeFileTypeTaskTempAttach(request);

		if(request.getIncoming().equals(TASK_FILE_ICOMING)) {

			TaskTemplateAttachment taskTemplateAttachment = taskTemplateAttachmentService.saveUpdateTaskTemplateAttachment(request);
			return new FileUploadResponse(taskTemplateAttachment);
		} else {
			
			ExpirationActivityAttachment expirationActivityAttachment = expirationActivityAttachmentService.saveUpdateExpirationActivityAttachment(request);
			return new FileUploadResponse(expirationActivityAttachment);
		}
	}

	private void setSizeFileTypeTaskTempAttach(FileUploadRequest request) {
		if(!isEmpty(request.getHttpFile().getStream())) {
			try {
				byte[] bytes = Files.readAllBytes(new File(request.getHttpFile().getFilePath()).toPath());
				long fileSize = bytes.length;
				if(BigInteger.valueOf(fileSize).divide(FileUtils.ONE_MB_BI).compareTo(new BigInteger(FILE_MAX_SIZE)) > 0) {
					throw new GeneralException("Maximum file size is " + FILE_MAX_SIZE + " MB");
				}
				request.getHttpFile().setSize(fileSize);

				Tika tika = new Tika();
				String contentType = tika.detect(request.getHttpFile().getStream());
				request.getHttpFile().setFileType(contentType);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
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

	@Override
	public void deleteFile(TaskTemplateAttachmentResult taskTemplateAttachmentResult) {
		
		try{
			File file = new File(taskTemplateAttachmentResult.getFilePath());
			if(file.delete()){
				taskTemplateAttachmentService.deleteTaskTemplateAttachment(taskTemplateAttachmentResult.getIdTaskTemplateAttachment());
				logger.info(file.getName() + " is deleted!");
			}else{
				logger.info("Delete operation is failed.");
			}
		} catch(Exception e) {
			logger.error("", e);
		}
	}
}
