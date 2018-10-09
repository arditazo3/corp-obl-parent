package com.tx.co.back_office.task.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface TaskOfficeRepository extends CrudRepository<TaskOffice, Long> {

	@Query("select to from TaskOffice to where to.taskTemplate = ?1 and to.office = ?2 and to.enabled <> 0")
	TaskOffice getTaskOfficeByTaskTemplateAndOffice(TaskTemplate taskTemplate, Office office);
}
