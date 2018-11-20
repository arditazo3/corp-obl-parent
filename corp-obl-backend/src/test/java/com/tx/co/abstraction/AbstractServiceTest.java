package com.tx.co.abstraction;

import com.tx.co.back_office.company.service.ICompanyService;
import com.tx.co.back_office.office.service.IOfficeService;
import com.tx.co.back_office.task.repository.TaskRepository;
import com.tx.co.back_office.task.service.ITaskService;
import com.tx.co.back_office.tasktemplate.service.ITaskTemplateService;
import com.tx.co.back_office.topic.service.ITopicService;
import com.tx.co.front_end.expiration.repository.ExpirationRepository;
import com.tx.co.front_end.expiration.service.ExpirationService;
import com.tx.co.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for SERVICE testing.
 *
 * @author aazo
 */
public class AbstractServiceTest extends AbstractApiTest {

    private IUserService userService;
    private ITaskService taskService;
    private ITaskTemplateService taskTemplateService;
    private TaskRepository taskRepository;
    private ExpirationService expirationService;
    private IOfficeService officeService;
    private ExpirationRepository expirationRepository;
    private ICompanyService companyService;
    private ITopicService topicService;

    public IUserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public ITaskService getTaskService() {
        return taskService;
    }

    @Autowired
    public void setTaskService(ITaskService taskService) {
        this.taskService = taskService;
    }

    public ITaskTemplateService getTaskTemplateService() {
        return taskTemplateService;
    }

    @Autowired
    public void setTaskTemplateService(ITaskTemplateService taskTemplateService) {
        this.taskTemplateService = taskTemplateService;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Autowired
    public void setExpirationRepository(ExpirationRepository expirationRepository) {
        this.expirationRepository = expirationRepository;
    }

    public ExpirationRepository getExpirationRepository() {
        return expirationRepository;
    }

    @Autowired
    public void setExpirationService(ExpirationService expirationService) {
        this.expirationService = expirationService;
    }

    public ExpirationService getExpirationService() {
        return expirationService;
    }

    public IOfficeService getOfficeService() {
        return officeService;
    }

    @Autowired
    public void setOfficeService(IOfficeService officeService) {
        this.officeService = officeService;
    }

	public ICompanyService getCompanyService() {
		return companyService;
	}

	@Autowired
	public void setCompanyService(ICompanyService companyService) {
		this.companyService = companyService;
	}

	public ITopicService getTopicService() {
		return topicService;
	}

	@Autowired
	public void setTopicService(ITopicService topicService) {
		this.topicService = topicService;
	}
}
