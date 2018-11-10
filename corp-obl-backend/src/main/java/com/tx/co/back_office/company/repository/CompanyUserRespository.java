package com.tx.co.back_office.company.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyUser;

public interface CompanyUserRespository extends CrudRepository<CompanyUser, Long> {
	
	@Transactional
	@Modifying
	@Query("update CompanyUser cur "
			+ "set cur.enabled = 0, "
			+ "cur.modificationDate = CURRENT_DATE, "
			+ "cur.modifiedBy = :username "
			+ "where cur.company = :company and "
			+ "username not in :userList")
	void updateCompanyUserNotEnable(@Param("company") Company company, @Param("userList") List<String> userList, @Param("username") String username);
	
	@Transactional
	@Modifying
	@Query("update CompanyUser cur set cur.enabled = 1, cur.modificationDate = CURRENT_DATE, cur.modifiedBy = :username where cur.company = :company and username in :userList")
	void updateCompanyUserEnable(@Param("company") Company company, @Param("userList") List<String> userList, @Param("username") String username);
	
	@Query("select cu from CompanyUser cu where cu.username = ?1 and cu.company = ?2 and cu.enabled <> 0")
	Optional<CompanyUser> getCompanyUserByUsernameAndCompanyId(String username, Company company);

	@Query("select cu from CompanyUser cu where cu.username = ?1 and cu.company = ?2 and cu.enabled <> 1")
	Optional<CompanyUser> getCompanyUserDisabledByUsernameAndCompanyId(String username, Company company);
}
