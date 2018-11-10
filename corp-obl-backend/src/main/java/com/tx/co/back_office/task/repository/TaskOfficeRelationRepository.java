package com.tx.co.back_office.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.model.TaskOfficeRelations;

public interface TaskOfficeRelationRepository extends CrudRepository<TaskOfficeRelations, Long> {

	
	@Query("select tor " + 
			" from TaskOfficeRelations tor " + 
			" where tor.enabled <> 0 " + 
			" and tor.username = :username " +
			" and tor.relationType = :relationType " + 
			" group by tor.idTaskOfficeRelation ")
	List<TaskOfficeRelations> getTaskOfficeRelationsByUsernameAndRelationType(@Param("username") String username, @Param("relationType") Integer relationType);
	
	@Query("select tor " + 
			" from TaskOfficeRelations tor " + 
			" where  tor.username = :username " +
			" and tor.taskOffice = :taskOffice ")
	Optional<TaskOfficeRelations> getTaskOfficeRelationsByUsernameAndTaskOffice(@Param("username") String username, @Param("taskOffice") TaskOffice taskOffice);
	
	@Transactional
	@Modifying
	@Query("update TaskOfficeRelations tor " + 
			" set tor.enabled = 0, "
			+ "tor.modificationDate = CURRENT_DATE, "
			+ "tor.modifiedBy = :username " + 
			" where tor.taskOffice = :taskOffice ")
	void updateTaskOfficeRelationsNotEnableByTaskOffice(@Param("taskOffice") TaskOffice taskOffice, @Param("username") String username);
}
