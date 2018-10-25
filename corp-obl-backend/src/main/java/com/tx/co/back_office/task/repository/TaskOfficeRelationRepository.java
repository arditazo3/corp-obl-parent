package com.tx.co.back_office.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tx.co.back_office.task.model.TaskOfficeRelations;

public interface TaskOfficeRelationRepository extends CrudRepository<TaskOfficeRelations, Long> {

	
	@Query("select tor " + 
			" from TaskOfficeRelations tor " + 
			" where tor.enabled <> 0 " + 
			" and tor.username = :username " +
			" and tor.relationType = :relationType " + 
			" group by tor.idTaskOfficeRelation ")
	List<TaskOfficeRelations> getTaskOfficeRelationsByUsernameAndRelationType(@Param("username") String username, @Param("relationType") Integer relationType);
	
}
