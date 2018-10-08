package com.tx.co.back_office.tasktemplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface TaskTemplateRepository extends CrudRepository<TaskTemplate, Long>  {

	@Query("select tt from TaskTemplate tt where tt.enabled <> 0 order by tt.description asc ")
	List<TaskTemplate> findAllOrderByDescriptionAsc();
	
	@Query("select tt from TaskTemplate tt " + 
			"left join tt.topic t " + 
			"left join t.topicConsultants tc " + 
			"left join tc.companyConsultant cc " + 
			"left join cc.company c " + 
			"left join c.companyUsers cu " + 
			"left join cu.user u on cu.username = u.username " +
			"where tt.enabled <> 0 "+
			"and u.username = :username " + 
			"group by tt.id order by tt.description asc")
	List<TaskTemplate> getTaskTemplatesByRole(String username);
}
