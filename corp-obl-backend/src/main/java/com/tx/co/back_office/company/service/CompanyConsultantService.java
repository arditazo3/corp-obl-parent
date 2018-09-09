package com.tx.co.back_office.company.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.repository.CompanyConsultantRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;

/**
 * Service for {@link com.tx.co.back_office.company.domain.CompanyConsultant}s.
 *
 * @author Ardit Azo
 */
@Service
public class CompanyConsultantService extends UpdateCacheData implements ICompanyConsultantService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(CompanyConsultantService.class);

	private CompanyConsultantRepository companyConsultantRepository;
	
	@Autowired
	public void setCompanyConsultantRepository(CompanyConsultantRepository companyConsultantRepository) {
		this.companyConsultantRepository = companyConsultantRepository;
	}

	@Override
	public List<CompanyConsultant> getCompanyConsultantByIdCompany(String idCompany) {
		return companyConsultantRepository.getCompanyConsultantByIdCompany(getCompanyById(Long.valueOf(idCompany)));
	}
	
	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

}
