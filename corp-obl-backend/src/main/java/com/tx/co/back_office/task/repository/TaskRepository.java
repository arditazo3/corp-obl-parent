package com.tx.co.back_office.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.back_office.task.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

	@Query("select t from Task t "
			+ "left join t.taskTemplate tt "
			+ "where t.enabled <> 0 "
			+ "group by t.idTask "
			+ "order by tt.description")
	List<Task> getTasks();
}
