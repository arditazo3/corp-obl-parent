package com.tx.co.back_office.topic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;

public interface TopicConsultantRepository extends CrudRepository<TopicConsultant, Long> {

	@Query("select tc from TopicConsultant tc where tc.enabled <> 0 order by tc.topic.description asc")
    List<TopicConsultant> findAllOrderByTopicDescription();
	
	@Query("select tc from TopicConsultant tc where tc.companyConsultant = :companyConsultant and tc.topic = :topic")
	Optional<TopicConsultant> findTopicConsultantByIds(@Param("companyConsultant") CompanyConsultant companyConsultant, @Param("topic") Topic topic);
	
	@Query("select tc from TopicConsultant tc where tc.companyConsultant.company = :company and  tc.topic = :topic")
	List<TopicConsultant> findTopicConsultantsByIds(@Param("company") Company company, @Param("topic") Topic topic);
}
