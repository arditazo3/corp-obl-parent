package com.tx.co.back_office.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface TaskRepository extends CrudRepository<Task, Long> {

	@Query("select t from Task t "
			+ "left join t.taskTemplate tt "
			+ "where t.enabled <> 0 "
			+ "group by t.idTask "
			+ "order by tt.description")
	List<Task> getTasks();
	
	@Query("select t from Task t "
			+ "left join t.taskTemplate tt "
			+ "where t.enabled <> 0 "
			+ "and tt = :taskTemplate "
			+ "group by t.idTask "
			+ "order by tt.description")
	List<Task> getTasksByTaskTemplate(@Param("taskTemplate") TaskTemplate taskTemplate);
}
