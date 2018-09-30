package com.tx.co.back_office.company.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.office.domain.Office;

import java.util.List;
import java.util.Optional;

public interface ICompanyService {

    List<Company> findAllCompany();
    
    List<Company> getCompaniesByRole();

    Company saveUpdateCompany(Company company);

    Optional<Company> findByIdCompany(Long idCompany);

    void deleteCompany(Long idCompany);
    
    void associateUserToCompany(Company company);
}
