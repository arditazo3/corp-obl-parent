package com.tx.co.back_office.task.service;

import static org.junit.Assert.assertNotNull;
import static com.tx.co.common.constants.AppConstants.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tx.co.abstraction.AbstractServiceTest;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

/**
 * Tests for the task service class.
 *
 * @author aazo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskServiceTest extends AbstractServiceTest {

	
	@Test
	public void getTasksAsAdmin() {
		
		setAuthorizationUsername(ADMIN);
		
		List<Task> tasks = getTaskService().getTasks();
		
		assertNotNull(tasks);
	}
	
	@Test
	public void getTasksAsForeign() {
		
		setAuthorizationUsername(FOREIGN);
		
		List<Task> tasks = getTaskService().getTasks();
		
		assertNotNull(tasks);
	}
	
	@Test
	public void saveTask_thenReturnTask() {
		
		setAuthorizationUsername(ADMIN);
		
		List<TaskTemplate> taskTemplates = getTaskTemplateService().searchTaskTemplateByDescr("Task"); 
		
		assertNotNull(taskTemplates);
		
		TaskTemplate taskTemplate = taskTemplates.get(0);
		
		Task task = new Task();
		
		task.setExpirationType(EXP_FIX_DAY);
		task.setRecurrence(REC_WEEKLY);
		task.setDay(1);
		
		task.setDaysBeforeShowExpiration(5);
		task.setDaysOfNotice(3);
		task.setFrequenceOfNotice(5);
		
		task.setModificationDate(new Date());
		task.setModifiedBy(ADMIN);
		task.setCreationDate(new Date());
		task.setCreatedBy(ADMIN);
		task.setEnabled(true);
		
		task.setTaskTemplate(taskTemplate);
		
		Task taskFound = getTaskService().saveUpdateTask(task);
		
		assertNotNull(taskFound.getIdTask());
	}
	
}
