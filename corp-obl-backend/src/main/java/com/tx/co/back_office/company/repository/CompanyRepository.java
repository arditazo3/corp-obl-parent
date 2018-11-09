package com.tx.co.back_office.company.repository;

import com.tx.co.back_office.company.domain.Company;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Long> {

	@Query("select c from Company c where c.description = ?1 and c.enabled <> 0")
	List<Company> findCompaniesByDescription(String description);

	@Query("select c from Company c where c.enabled <> 0 order by c.description asc ")
	List<Company> findAllByOrderByDescriptionAsc();

	@Query("select c " + 
			" from Company c " + 
			" left join c.companyUsers cu " + 
			" left join cu.user u " + 
			"where c.enabled <> 0 and cu.enabled <> 0 and u.enabled <> 0 " + 
			"and u.username = :username " + 
			"group by c.id order by c.description asc")
	List<Company> getCompaniesByRole(@Param("username") String username);
	
	@Query("select c " + 
			" from Company c " + 
			" left join c.companyUsers cu " + 
			" left join cu.user u " + 
			" where c.enabled <> 0 and cu.enabled <> 0 and u.enabled <> 0 " + 
			" and cu.username = :username and cu.companyAdmin <> 0 " + 
			" group by c.id order by c.description asc")
	List<Company> getCompaniesByRoleUser(@Param("username") String username);
	
}
