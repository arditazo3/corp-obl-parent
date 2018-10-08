package com.tx.co.back_office.office.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tx.co.back_office.office.domain.Office;

public interface OfficeRepository extends CrudRepository<Office, Long> {

	@Query("select o from Office o where o.enabled <> 0 order by o.description asc ")
	List<Office> findAllByOrderByDescriptionAsc();

	@Query("select o from Office o where o.description = ?1 and o.enabled <> 0")
	List<Office> findOfficesByDescription(String description);
	
	@Query("select o " + 
			" from Office o " + 
			" left join o.company c " + 
			" left join c.companyUsers cu " + 
			" left join cu.user u " + 
			" where c.enabled <> 0 and cu.enabled <> 0 and u.enabled <> 0 " + 
			" and u.username = :username " + 
			" group by o.id order by o.description asc")
	List<Office> getOfficesByRole(@Param("username") String username);

}
