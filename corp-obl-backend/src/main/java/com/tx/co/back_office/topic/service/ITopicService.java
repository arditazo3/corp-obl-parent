package com.tx.co.back_office.topic.service;

import java.util.List;
import java.util.Optional;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;


public interface ITopicService {

	List<Topic> findAllTopic();

	Topic saveUpdateTopic(Topic topic);

	Optional<Topic> findByIdTopic(Long idTopic);

	void deleteTopic(Long idTopic);

	List<Topic> findAllOrderByDescriptionAsc();
	
	List<Topic> getTopicsByRoleList();
	
	List<Topic> getTopicsByRole();
	
	TopicConsultant saveUpdateTopicConsultant(TopicConsultant topicConsultant);
	
	void deleteTopicConsultant(TopicConsultant topicConsultant);
	
	void deleteTopicConsultants(Company company, Topic topic);
	
	List<TopicConsultant> findAllOrderByTopicDescription();
	
	Optional<TopicConsultant> findByIdTopicConsultant(TopicConsultant topicConsultant);
	
}
