package com.tx.co.back_office.company.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
	public void setCompanSyTopicRepository(CompanyTopicRepository companyTopicRepository) {   
		this.companyTopicRepository = companyTopicRepository;
	}

	/* (non-Javadoc)
	 * @see com.tx.co.back_office.company.service.ICompanyTopicService#getCompanyTopicByIdCompany(java.lang.String)
	 * 
	 * Retrieve Topic from cache in order to get the translations
	 */
	@Override
	public List<CompanyTopic> getCompanyTopicByIdCompany(String idCompany) {
		
		List<CompanyTopic> companyTopics = companyTopicRepository.getCompanyTopicByIdCompany(getCompanyById(Long.valueOf(idCompany)));
		
		for (CompanyTopic companyTopic : companyTopics) {
			Long idTopic = companyTopic.getTopic().getIdTopic();
			companyTopic.setTopic(getTopicById(idTopic, idCompany));
		}
		return companyTopics;
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
	
}
