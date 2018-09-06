package com.tx.co.common.api.provider;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.api.model.CompanyUserResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyUser;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.security.exception.GeneralException;

public abstract class ObjectResult {

	/**
     * Map a {@link Company} instance to a {@link CompanyResult} instance.
     *
     * @param company
     * @return UserResult
     */
    public CompanyResult toCompanyResult(Company company) {
        CompanyResult result = new CompanyResult();
        result.setIdCompany(company.getIdCompany());
        result.setDescription(company.getDescription());
        
        if(!isEmpty(company.getCompanyUsers())) {
        	result.setUsersAssociated(new ArrayList<>());
        	for (CompanyUser companyUser : company.getCompanyUsers()) {
        		result.getUsersAssociated().add(toCompanyUserResult(companyUser));
			}
        }
        return result;
    }

    public Company toCompany(CompanyResult companyResult) {
        Company company = new Company();
        if(isEmpty(companyResult)) {
            throw new GeneralException("The form is empty");
        }
        if(!isEmpty(companyResult.getIdCompany())) {
            company.setIdCompany(companyResult.getIdCompany());
        }
        if(!isEmpty(companyResult.getDescription())) {
            company.setDescription(companyResult.getDescription().trim());
        }
        if(!isEmpty(companyResult.getUsersAssociated())) {
        	for (CompanyUserResult companyUserResylt : companyResult.getUsersAssociated()) {
        		company.getCompanyUsers().add(toCompanyUser(company, companyUserResylt));
			}
        }

        return company;
    }
    
    public CompanyUserResult toCompanyUserResult(CompanyUser companyUser) {
    	CompanyUserResult result = new CompanyUserResult();
    	result.setIdCompanyUser(companyUser.getIdCompanyUser());
    	result.setUsername(companyUser.getUsername());
    	result.setCompanyAdmin(companyUser.getCompanyAdmin());
    	return result;
    }
    
    public CompanyUser toCompanyUser(Company company, CompanyUserResult companyUserResult) {
    	CompanyUser companyUser = new CompanyUser();
    	companyUser.setIdCompanyUser(companyUserResult.getIdCompanyUser());
    	companyUser.setUsername(companyUserResult.getUsername());
    	companyUser.setCompany(company);
    	if(isEmpty(companyUserResult.getCompanyAdmin())) {
    		companyUser.setCompanyAdmin(false);
    	} else {
    		companyUser.setCompanyAdmin(companyUserResult.getCompanyAdmin());
    	}
    	return companyUser;
    }
    
    public List<CompanyUser> toCompanyUserList(Company company, List<CompanyUserResult> companyUserResultList) {
    	List<CompanyUser> companyUserList = new ArrayList<>();
    	if(!isEmpty(companyUserResultList)) {
    		for (CompanyUserResult companyUserResult : companyUserResultList) {
    			companyUserList.add(toCompanyUser(company, companyUserResult));
			}
    	}
    	return companyUserList;
    }
	
    /**
     * Map a {@link Company} instance to a {@link CompanyResult} instance.
     *
     * @param company
     * @return UserResult
     */
    public OfficeResult toOfficeResult(Office office) {
    	OfficeResult result = new OfficeResult();
        result.setIdOffice(office.getIdOffice());
        result.setDescription(office.getDescription());
        if(!isEmpty(office.getCompany())) {
        	
        	result.setCompany(toCompanyResult(office.getCompany()));
        }
        return result;
    }
    
    public Office toOffice(OfficeResult officeResult) {
    	Office office = new Office();
    	if(isEmpty(officeResult)) {
    		throw new GeneralException("The form is empty");
    	}
    	if(!isEmpty(officeResult.getIdOffice())) {
    		office.setIdOffice(officeResult.getIdOffice());
        }
        if(!isEmpty(officeResult.getDescription())) {
        	office.setDescription(officeResult.getDescription().trim());
        }
        if(!isEmpty(officeResult.getDescription())) {
        	office.setCompany(toCompany(officeResult.getCompany()));
        }
        return office;
    }
    
}
