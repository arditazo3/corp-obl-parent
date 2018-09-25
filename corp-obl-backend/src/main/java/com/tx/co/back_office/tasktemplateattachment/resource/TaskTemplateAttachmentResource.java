package com.tx.co.back_office.tasktemplateattachment.resource;

import static com.tx.co.common.constants.ApiConstants.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

	@GET
	@Path(DOWNLOAD_FILES)
	public Response fileUpload(@QueryParam("filePath") String filePath) { 

		try {
			
			File file = new File(filePath);
			InputStream is = new FileInputStream(filePath);
			
			String contentType = Files.probeContentType(file.toPath());

//			Response.ResponseBuilder response = Response.ok((Object) file);
//			response.header("Content-Disposition", "attachment; filename="+file.getName());
//			response.header("Content-Type", contentType);
//			response.header("Content-Length", file.length());
//			return response.build();
			
			// create a byte array of the file in correct format
			byte[] docStream = IOUtils.toByteArray(is);

			return Response
			            .ok(docStream, contentType)
			            .header("content-disposition", "attachment; filename=" + file.getName())
			            .build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}


}
