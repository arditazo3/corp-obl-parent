package com.tx.co.back_office.company.repository;

import java.util.List;

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
	@Query("update CompanyUser cur set cur.enabled = 0 where cur.company = :company and username not in :userList")
	void updateCompanyUserNotEnable(@Param("company") Company company, @Param("userList") List<String> userList);

}
