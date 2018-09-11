package com.tx.co.back_office.company.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.company.repository.CompanyTopicRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;

/**
 * Service for {@link com.tx.co.back_office.company.domain.CompanyTopic}s.
 *
 * @author Ardit Azo
 */
@Service
public class CompanyTopicService extends UpdateCacheData implements ICompanyTopicService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(CompanyTopicService.class);
	
	private CompanyTopicRepository companyTopicRepository;
	
	@Autowired
	public void setCompanyTopicRepository(CompanyTopicRepository companyTopicRepository) {
		this.companyTopicRepository = companyTopicRepository;
	}

	@Override
	public List<CompanyTopic> getCompanyTopicByIdCompany(String idCompany) {
		return companyTopicRepository.getCompanyTopicByIdCompany(getCompanyById(Long.valueOf(idCompany)));
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
	
}
