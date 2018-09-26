package com.tx.co.back_office.tasktemplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.topic.domain.Topic;

public interface TaskTemplateRepository extends CrudRepository<TaskTemplate, Long>  {

	@Query("select tt from TaskTemplate tt where tt.enabled <> 0 order by tt.description asc ")
	List<TaskTemplate> findAllOrderByDescriptionAsc();
	
//	@Query("select tt from TaskTemplate tt "
//			+ "left join tt.topic top "
//			+ "left join top.companyTopic ct "
//			+ "left join ct.company c "
//			+ "where tt.enabled <> 0 and top.enabled <> 0 and ct.enabled <> 0 and c.enabled <> 0"
//			+ "and tt.description like :description "
//			+ "or (top in :topicsList and c in :companiesList) "
//			+ "group by tt.idTaskTemplate "
//			+ "order by tt.description asc")
//	List<TaskTemplate> searchTaskTemplate(@Param("description") String description, @Param("topicsList") List<Topic> topicsList, @Param("companiesList") List<Company> companiesList);
}
