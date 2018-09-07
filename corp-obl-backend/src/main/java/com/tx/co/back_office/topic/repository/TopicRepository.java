package com.tx.co.back_office.topic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.back_office.topic.domain.Topic;

public interface TopicRepository extends CrudRepository<Topic, Long> {

	@Query("SELECT t FROM Topic t WHERE t.enabled <> 0 order by t.description asc ")
    List<Topic> findAllByOrderByDescriptionAsc();
	
	@Query("SELECT t FROM Topic t WHERE t.description = ?1 and t.enabled <> 0")
    List<Topic> findTopicsByDescription(String description);
}
