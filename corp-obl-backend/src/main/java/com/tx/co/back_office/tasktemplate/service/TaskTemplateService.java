package com.tx.co.back_office.tasktemplate.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplate.repository.TaskTemplateRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;

/**
 * Service for {@link com.tx.co.back_office.tasktemplate.domain.TaskTemplate}s.
 *
 * @author aazo
 */
@Service
public class TaskTemplateService extends UpdateCacheData implements ITaskTemplateService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(TaskTemplateService.class);

	private TaskTemplateRepository taskTemplateRepository;

	@Autowired
	public void setTaskTemplateRepository(TaskTemplateRepository taskTemplateRepository) {
		this.taskTemplateRepository = taskTemplateRepository;
	}

	@Override
	public TaskTemplate saveUpdateTaskTemplate(TaskTemplate taskTemplate) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskTemplate taskTemplateStored = null;

		// New Task template
		if(isEmpty(taskTemplate.getIdTaskTemplate())) {
			taskTemplate.setCreationDate(new Date());
			taskTemplate.setCreatedBy(username);
			taskTemplate.setEnabled(true);
			taskTemplateStored = taskTemplate;
		} else { // Existing Task template
			taskTemplateStored = getTaskTemplateById(taskTemplate.getIdTaskTemplate());
			taskTemplateStored.setDescription(taskTemplate.getDescription());
		}

		taskTemplateStored.setTopic(taskTemplate.getTopic());
		taskTemplateStored.setModificationDate(new Date());
		taskTemplateStored.setModifiedBy(username);

		taskTemplateStored = taskTemplateRepository.save(taskTemplateStored);

		updateTaskTemplateCache(taskTemplateStored, false);

		return taskTemplateStored;
	}
	
	

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public Optional<TaskTemplate> findByIdTaskTemplate(Long idTaskTemplate) {
		return taskTemplateRepository.findById(idTaskTemplate);
	}


}
