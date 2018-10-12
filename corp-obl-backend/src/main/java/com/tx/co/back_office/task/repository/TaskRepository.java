package com.tx.co.back_office.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface TaskRepository extends CrudRepository<Task, Long> {

	@Query("select t from Task t " + 
			"left join t.taskTemplate tt " + 
			"where t.enabled <> 0 " + 
			"group by t.idTask " + 
			"order by tt.description")
	List<Task> getTasks();
	
	@Query("select t from Task t " + 
			"left join t.taskTemplate tt " +
			"left join tt.topic t " + 
			"left join t.topicConsultants tc " + 
			"left join tc.companyConsultant cc " + 
			"left join cc.company c " + 
			"left join c.companyUsers cu " + 
			"where tt.enabled <> 0 "+
			"and cu.username = :username " + 
			"group by tt.id order by tt.description asc")
	List<TaskTemplate> getTasksByRole(@Param("username") String username);
	
	@Query("select t from Task t "
			+ "left join t.taskTemplate tt "
			+ "where t.enabled <> 0 "
			+ "and tt = :taskTemplate "
			+ "group by t.idTask "
			+ "order by tt.description")
	List<Task> getTasksByTaskTemplate(@Param("taskTemplate") TaskTemplate taskTemplate);
}
