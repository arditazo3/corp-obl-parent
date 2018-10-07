package com.tx.co.back_office.tasktemplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface TaskTemplateRepository extends CrudRepository<TaskTemplate, Long>  {

	@Query("select tt from TaskTemplate tt where tt.enabled <> 0 order by tt.description asc ")
	List<TaskTemplate> findAllOrderByDescriptionAsc();
	
	@Query(value = "select tt.* from co_tasktemplate tt " + 
			"left join co_topic t on tt.topic_id = t.id " + 
			"left join co_topicconsultant tc on t.id = tc.topic_id " + 
			"left join co_companyconsultant cc on tc.consultantcompany_id = cc.id " + 
			"left join co_company c on cc.company_id = c.id " + 
			"left join co_companyuser cu on c.id = cu.company_id " + 
			"left join co_user u on cu.username = u.username " +
			"left join co_userrole ur on u.username = ur.username " + 
			"where tt.enabled <> 0 "+
			"and ur.roleuid in (:authorities) " + 
			"group by tt.id order by tt.description asc", nativeQuery = true)
	List<TaskTemplate> getTaskTemplatesByRole(List<String> authorities);
}
