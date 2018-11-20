package com.tx.co.back_office.topic.service;

import static com.tx.co.common.constants.AppConstants.*;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tx.co.abstraction.AbstractServiceTest;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.topic.domain.Topic;

/**
 * Tests for the topic service class.
 *
 * @author aazo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicServiceTest extends AbstractServiceTest {

	@Test
	public void getTopicsAsForeign() {

		setAuthorizationUsername(FOREIGN);

		List<Topic> topics = getTopicService().getTopicsByRole();

		assertNotNull(topics);
	}

	@Test
	public void getTopicsAsInland() {

		setAuthorizationUsername(INLAND);

		List<Topic> topics = getTopicService().getTopicsByRole();

		assertNotNull(topics);
	}
	
	@Test
	public void saveTopic_thenReturnTopic() {
		
		setAuthorizationUsername(FOREIGN);
		
		Topic topic = new Topic();
		
		topic.setDescription("Test topic");
		topic.setModifiedBy(FOREIGN);
		topic.setCreationDate(new Date());
		topic.setCreatedBy(FOREIGN);
		topic.setEnabled(true);
		
		Topic topicFound = getTopicService().saveUpdateTopic(topic);
		
		assertNotNull(topicFound.getIdTopic());
	}
}
