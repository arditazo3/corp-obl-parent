package com.tx.co.back_office.company.service;

import java.util.List;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;

public interface ICompanyConsultantService {

	List<CompanyConsultant> getCompanyConsultantByIdCompany(String idCompany);
}
