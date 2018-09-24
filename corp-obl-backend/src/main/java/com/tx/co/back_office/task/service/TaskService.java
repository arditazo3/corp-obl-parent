package com.tx.co.back_office.task.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.service.ICompanyService;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.repository.TaskRepository;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.service.ITopicService;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.user.domain.User;

/**
 * Service for {@link TaskService}s.
 *
 * @author aazo
 */
@Service
public class TaskService extends UpdateCacheData implements ITaskService, IUserManagementDetails {

	private TaskRepository taskRepository;
	private ICompanyService companyService;
	private ITopicService topicService;

	@Autowired
	public void setTaskRepository(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Autowired
	public void setCompanyService(ICompanyService companyService) {
		this.companyService = companyService;
	}

	@Autowired
	public void setTopicService(ITopicService topicService) {
		this.topicService = topicService;
	}

	@Override
	public List<Task> getTasksByDescriptionOrCompaniesOrTopics(String description, List<Company> companies,
			List<Topic> topics) {

		return null;
	}

	@Override
	public List<Task> getTasks() {

		User userLoggedIn = getTokenUserDetails().getUser();
		
		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			return taskRepository.getTasks();
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			List<Company> companies = companyService.getCompaniesByRole();
			List<Topic> topics = topicService.getTopicsByRole();
			// FIXME
			return new ArrayList<>();
		}
		return new ArrayList<>();
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

}
