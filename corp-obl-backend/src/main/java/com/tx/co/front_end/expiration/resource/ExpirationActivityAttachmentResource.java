package com.tx.co.front_end.expiration.resource;

import static com.tx.co.common.constants.ApiConstants.*;
import static com.tx.co.common.constants.AppConstants.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.tasktemplateattachment.handler.IFileUploadHandler;
import com.tx.co.back_office.tasktemplateattachment.model.file.HttpFile;
import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.back_office.tasktemplateattachment.model.response.FileUploadResponse;
import com.tx.co.front_end.expiration.api.model.ExpirationActivityAttachmentResult;
import com.tx.co.security.exception.GeneralException;

@Component
@Path(FRONT_END)
public class ExpirationActivityAttachmentResource {

	private static final Logger logger = LogManager.getLogger(ExpirationActivityAttachmentResource.class);
	
	@Context
	private UriInfo uriInfo; 

	private final IFileUploadHandler fileUploadHandler;

	@Autowired
	public ExpirationActivityAttachmentResource(IFileUploadHandler fileUploadHandler) {
		this.fileUploadHandler = fileUploadHandler;
	}
	
	@POST
	@Path(UPLOAD_FILES_EXP)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fileUploadExp(@FormDataParam("file") InputStream stream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("idExpirationActivityAttachment") Long idExpirationActivityAttachment) {

		logger.info("fileUpload - Path: " + UPLOAD_FILES_EXP);
		
		// Create the HttpFile:
		HttpFile httpFile = new HttpFile(fileDetail.getName(), fileDetail.getFileName(), fileDetail.getParameters(), stream);

		// Create the FileUploadRequest:
		FileUploadRequest fileUploadRequest = new FileUploadRequest(idExpirationActivityAttachment, httpFile, EXP_FILE_ICOMING);

		// Handle the File Upload:
		FileUploadResponse result = fileUploadHandler.handle(fileUploadRequest);

		return Response
				.status(200)
				.entity(result)
				.build();
	}
	
	@POST
	@Path(DOWNLOAD_FILES_EXP)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadFileExp(ExpirationActivityAttachmentResult expirationActivityAttachmentResult) { 

		logger.info("downloadFile expiration - Path: " + DOWNLOAD_FILES_EXP);
		
		StreamingOutput fileStream =  new StreamingOutput()
		{
		    @Override
		    public void write(java.io.OutputStream output)
		    {
		        try
		        {
		            java.nio.file.Path path = Paths.get(expirationActivityAttachmentResult.getFilePath());
		            byte[] data = Files.readAllBytes(path);
		            output.write(data); 
		            output.flush();
		        }
		        catch (Exception e)
		        {
		            throw new GeneralException("File Not Found !!");
		        }
		    }
		};
		return Response
		        .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
		        .header("content-disposition","attachment; filename = " + expirationActivityAttachmentResult.getFileName())
		        .build();
	}

    @PUT
    @Path(REMOVE_FILES_EXP)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteFileExp(ExpirationActivityAttachmentResult expirationActivityAttachmentResult) {

    	logger.info("deleteFile - Path: " + REMOVE_FILES_EXP);
    	
    	fileUploadHandler.deleteFileExp(expirationActivityAttachmentResult);

        return Response.noContent().build();
    }
}
