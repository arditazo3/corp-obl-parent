package com.tx.co.back_office.tasktemplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface TaskTemplateRepository extends CrudRepository<TaskTemplate, Long>  {

	@Query("select tt from TaskTemplate tt where tt.enabled <> 0 order by tt.description asc ")
	List<TaskTemplate> findAllOrderByDescriptionAsc();
}
