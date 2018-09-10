package com.tx.co.back_office.company.service;

import java.util.List;
import java.util.Optional;

import com.tx.co.back_office.company.domain.CompanyConsultant;

public interface ICompanyConsultantService {

	List<CompanyConsultant> getCompanyConsultantByIdCompany(String idCompany);
	
	public CompanyConsultant saveUpdateCompanyConsultant(CompanyConsultant companyConsultant);
	
	List<CompanyConsultant> findAll();
	
	Optional<CompanyConsultant> findByIdCompanyConsultant(Long IdCompanyConsultant);
	
	void deleteCompanyConsultant(Long idCompanyConsultant);
}
