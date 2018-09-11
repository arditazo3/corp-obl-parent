package com.tx.co.back_office.company.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.domain.CompanyTopic;

public interface CompanyTopicRepository extends CrudRepository<CompanyTopic, Long> {

	@Query("select ct from CompanyTopic ct where ct.enabled <> 0 and ct.company = :company order by ct.topic.description asc")
	List<CompanyTopic> getCompanyTopicByIdCompany(@Param("company") Company company);
}
