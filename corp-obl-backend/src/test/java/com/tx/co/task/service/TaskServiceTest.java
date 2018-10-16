package com.tx.co.task.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tx.co.abstraction.AbstractServiceTest;
import com.tx.co.back_office.task.model.Task;

/**
 * Tests for the task service class.
 *
 * @author aazo
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskServiceTest extends AbstractServiceTest {

	
	@Test
	public void getTasks() {
		
		setAuthorizationUsername("ADMIN");
		
		List<Task> tasks = getTaskService().getTasks();
		
		assertNotNull(tasks);
	}
	
}
