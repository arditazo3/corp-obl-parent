package com.tx.co.back_office.company.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;

public interface CompanyConsultantRepository extends CrudRepository<CompanyConsultant, Long> {

	@Query("select cc from CompanyConsultant cc where cc.enabled <> 0 and cc.company = :company order by cc.name asc")
	List<CompanyConsultant> getCompanyConsultantByIdCompany(Company company);
}
