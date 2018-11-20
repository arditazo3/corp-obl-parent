package com.tx.co.back_office.tasktemplate.service;

import static com.tx.co.common.constants.AppConstants.ADMIN;
import static com.tx.co.common.constants.AppConstants.EXP_FIX_DAY;
import static com.tx.co.common.constants.AppConstants.FOREIGN;
import static com.tx.co.common.constants.AppConstants.REC_WEEKLY;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tx.co.abstraction.AbstractServiceTest;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.topic.domain.Topic;

/**
 * Tests for the task template service class.
 *
 * @author aazo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskTemplateServiceTest extends AbstractServiceTest {

	@Test
	public void getTaskTemplatesAsAdmin() {
		
		setAuthorizationUsername(ADMIN);
		
		List<Task> tasks = getTaskTemplateService().getTasksForTable();
		
		assertNotNull(tasks);
	}
	
	@Test
	public void getTaskTemplatesAsForeign() {
		
		setAuthorizationUsername(FOREIGN);
		
		List<Task> tasks = getTaskTemplateService().getTasksForTable();
		
		assertNotNull(tasks);
	}
	
	@Test
	public void saveTaskTemplate_thenReturnTaskTemplate() {
		
		setAuthorizationUsername(ADMIN);
		
		List<Topic> topics = getTopicService().getTopicsByRole();
		
		assertNotNull(topics);
		
		Topic topic = topics.get(0);
		
		TaskTemplate taskTemplate = new TaskTemplate();
		
		taskTemplate.setDescription("Test tasktemplate");
		taskTemplate.setExpirationType(EXP_FIX_DAY);
		taskTemplate.setRecurrence(REC_WEEKLY);
		taskTemplate.setDay(1);
		taskTemplate.setExpirationClosableBy(1);
		
		taskTemplate.setIsRapidConfiguration(false);
		
		taskTemplate.setDaysBeforeShowExpiration(5);
		taskTemplate.setDaysOfNotice(3);
		taskTemplate.setFrequenceOfNotice(5);
		
		taskTemplate.setModificationDate(new Date());
		taskTemplate.setModifiedBy(ADMIN);
		taskTemplate.setCreationDate(new Date());
		taskTemplate.setCreatedBy(ADMIN);
		taskTemplate.setEnabled(true);
		
		taskTemplate.setTopic(topic);
		
		TaskTemplate taskTemplateFound = getTaskTemplateService().saveUpdateTaskTemplate(taskTemplate, null);
		
		assertNotNull(taskTemplateFound.getIdTaskTemplate());
	}
}
