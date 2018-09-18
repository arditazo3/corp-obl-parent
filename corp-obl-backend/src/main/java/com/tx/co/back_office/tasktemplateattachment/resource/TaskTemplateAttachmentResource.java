package com.tx.co.back_office.tasktemplateattachment.resource;

import static com.tx.co.common.constants.ApiConstants.BACK_OFFICE;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
import com.tx.co.common.api.provider.ObjectResult;

import static com.tx.co.common.constants.ApiConstants.*;

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
		HttpFile httpFile = new HttpFile(fileDetail.getName(), fileDetail.getFileName(), fileDetail.getType(), fileDetail.getSize(), fileDetail.getParameters(), stream);

		// Create the FileUploadRequest:
		FileUploadRequest fileUploadRequest = new FileUploadRequest(idTaskTemplate, httpFile);

		// Handle the File Upload:
		FileUploadResponse result = fileUploadHandler.handle(fileUploadRequest);

		return Response
				.status(200)
				.entity(result)
				.build();
	}

}
