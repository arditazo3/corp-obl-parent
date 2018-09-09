package com.tx.co.back_office.topic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.topic.domain.Topic;

public interface TopicRepository extends CrudRepository<Topic, Long> {

	@Query("SELECT t FROM Topic t WHERE t.enabled <> 0 order by t.description asc ")
    List<Topic> findAllByOrderByDescriptionAsc();
	
	@Query("SELECT t FROM Topic t WHERE t.description = ?1 and t.enabled <> 0")
    List<Topic> findTopicsByDescription(String description);
	
	@Transactional
	@Modifying
	@Query("update CompanyTopic ct set ct.enabled = 0 where ct.topic = :topic and ct.company not in :companyListIncluded")
	void updateCompanyTopicNotEnable(@Param("topic") Topic topic, @Param("companyListIncluded")  List<Company> companyListIncluded);
	
	@Query("select ct from CompanyTopic ct where ct.company = :company and ct.topic = :topic")
	Optional<CompanyTopic> getCompanyTopicByCompanyAndTopic(Company company, Topic topic);
}
