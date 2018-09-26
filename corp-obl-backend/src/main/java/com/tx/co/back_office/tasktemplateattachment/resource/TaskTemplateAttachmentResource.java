package com.tx.co.back_office.tasktemplateattachment.resource;

import static com.tx.co.common.constants.ApiConstants.*;

import java.io.File;
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

import com.tx.co.back_office.tasktemplateattachment.api.model.TaskTemplateAttachmentResult;
import com.tx.co.back_office.tasktemplateattachment.handler.IFileUploadHandler;
import com.tx.co.back_office.tasktemplateattachment.model.file.HttpFile;
import com.tx.co.back_office.tasktemplateattachment.model.request.FileUploadRequest;
import com.tx.co.back_office.tasktemplateattachment.model.response.FileUploadResponse;
import com.tx.co.common.api.provider.ObjectResult;
import com.tx.co.security.exception.GeneralException;

@Component
@Path(BACK_OFFICE)
public class TaskTemplateAttachmentResource extends ObjectResult {

	private static final Logger logger = LogManager.getLogger(TaskTemplateAttachmentResource.class);

	@Context
	private UriInfo uriInfo; 

	private final IFileUploadHandler fileUploadHandler;

	@Autowired
	public TaskTemplateAttachmentResource(IFileUploadHandler fileUploadHandler) {
		this.fileUploadHandler = fileUploadHandler;
	}

	@POST
	@Path(UPLOAD_FILES)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fileUpload(@FormDataParam("file") InputStream stream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("idTaskTemplate") Long idTaskTemplate) {

		// Create the HttpFile:
		HttpFile httpFile = new HttpFile(fileDetail.getName(), fileDetail.getFileName(), fileDetail.getParameters(), stream);

		// Create the FileUploadRequest:
		FileUploadRequest fileUploadRequest = new FileUploadRequest(idTaskTemplate, httpFile);

		// Handle the File Upload:
		FileUploadResponse result = fileUploadHandler.handle(fileUploadRequest);

		return Response
				.status(200)
				.entity(result)
				.build();
	}

	@POST
	@Path(DOWNLOAD_FILES)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fileUpload(TaskTemplateAttachmentResult taskTemplateAttachmentResult) { 

		StreamingOutput fileStream =  new StreamingOutput()
		{
		    @Override
		    public void write(java.io.OutputStream output)
		    {
		        try
		        {
		            java.nio.file.Path path = Paths.get(taskTemplateAttachmentResult.getFilePath());
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
		        .header("content-disposition","attachment; filename = " + taskTemplateAttachmentResult.getFileName())
		        .build();
	}

    @PUT
    @Path(REMOVE_FILES)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
 //   @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response deleteFile(TaskTemplateAttachmentResult taskTemplateAttachmentResult) {

    	fileUploadHandler.deleteFile(taskTemplateAttachmentResult);

        return Response.noContent().build();
    }

}
