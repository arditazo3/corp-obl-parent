package com.tx.co.common.api.config;

import com.tx.co.back_office.company.resource.CompanyResource;
import com.tx.co.back_office.office.resource.OfficeResource;
import com.tx.co.back_office.task.resource.TaskResource;
import com.tx.co.back_office.tasktemplate.resource.TaskTemplateResource;
import com.tx.co.back_office.tasktemplateattachment.exceptions.FileUploadExceptionMapper;
import com.tx.co.back_office.tasktemplateattachment.resource.TaskTemplateAttachmentResource;
import com.tx.co.back_office.topic.resource.TopicResource;
import com.tx.co.common.api.provider.ObjectMapperProvider;
import com.tx.co.common.translation.resource.TranslationResource;
import com.tx.co.front_end.expiration.resource.ExpirationResource;
import com.tx.co.security.api.exceptionmapper.AccessDeniedExceptionMapper;
import com.tx.co.security.api.exceptionmapper.AuthenticationExceptionMapper;
import com.tx.co.security.api.exceptionmapper.AuthenticationTokenRefreshmentExceptionMapper;
import com.tx.co.security.api.exceptionmapper.GeneralExceptionMapper;
import com.tx.co.security.api.resource.AuthenticationResource;
import com.tx.co.user.resource.UserResource;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

import static com.tx.co.common.constants.ApiConstants.APP_PATH;

/**
 * Jersey configuration class.
 *
 * @author aazo
 */
@Component
@ApplicationPath(APP_PATH)
public class JerseyConfig extends ResourceConfig {

	/**
	 * Jersey URL configuration
	 */
	public JerseyConfig() {

		/**
		 * Application Resource
		 * */
		register(AuthenticationResource.class);
		register(UserResource.class);
		register(CompanyResource.class);
		register(OfficeResource.class);
		register(TopicResource.class);
		register(TaskTemplateResource.class);
		register(TaskTemplateAttachmentResource.class);
		register(TranslationResource.class);
		register(TaskResource.class);
		
		register(ExpirationResource.class);

		/**
		 * General exception
		 * */
		register(AccessDeniedExceptionMapper.class);
		register(AuthenticationExceptionMapper.class);
		register(AuthenticationTokenRefreshmentExceptionMapper.class);
		register(GeneralExceptionMapper.class);
		register(MultiPartFeature.class);
        // Register Exception Mappers for returning API Errors:
        register(FileUploadExceptionMapper.class);

		register(ObjectMapperProvider.class);
	}
}