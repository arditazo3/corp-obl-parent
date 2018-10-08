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

	@Query(value = "select c.* " + 
			"from co_company c " + 
			"       left join co_companyuser cu on c.id = cu.company_id " + 
			"       left join co_user u on cu.username = u.username " + 
			"       left join co_userrole ur on u.username = ur.username " + 
			"where c.enabled <> 0 and cu.enabled <> 0 and u.enabled <> 0 " + 
			"and ur.roleuid in (:authorities) " + 
			"group by c.id order by c.description asc", nativeQuery = true)
	List<Company> getCompaniesByRole(@Param("authorities") List<String> authorities);
	
	@Query(value = "select c.* " + 
			"from co_company c " + 
			"       left join co_companyuser cu on c.id = cu.company_id " + 
			"       left join co_user u on cu.username = u.username " + 
			"       left join co_userrole ur on u.username = ur.username " + 
			"where c.enabled <> 0 and cu.enabled <> 0 and u.enabled <> 0 " + 
			"and ur.roleuid in (:authorities) " + 
			"group by c.id order by c.description asc", nativeQuery = true)
	List<Company> getCompaniesByRoleTest(@Param("authorities") List<String> authorities);

	@Query(value = "select c.* " + 
			"from co_company c " + 
			"       left join co_companyuser cu on c.id = cu.company_id " + 
			"       left join co_user u on cu.username = u.username " + 
			"       left join co_userrole ur on u.username = ur.username " + 
			"where c.enabled <> 0 and cu.enabled <> 0 and u.enabled <> 0 " + 
			"and ur.roleuid in (:authorities) and cu.username = :username and cu.companyadmin <> 0 " + 
			"group by c.id order by c.description asc", nativeQuery = true)
	List<Company> getCompaniesByRoleUser(@Param("authorities") List<String> authorities, @Param("username") String username);
}
