package com.tx.co.back_office.topic.service;

import java.util.List;
import java.util.Optional;

import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;


public interface ITopicService {

	List<Topic> findAllTopic();

	Topic saveUpdateTopic(Topic topic);

	Optional<Topic> findByIdTopic(Long idTopic);

	void deleteTopic(Long idTopic);

	List<Topic> findAllByOrderByDescriptionAsc();
	
	TopicConsultant saveUpdateTopicConsultant(TopicConsultant topicConsultant);
}
