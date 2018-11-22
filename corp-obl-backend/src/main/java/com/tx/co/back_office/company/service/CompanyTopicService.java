package com.tx.co.back_office.company.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.company.repository.CompanyTopicRepository;
import com.tx.co.back_office.topic.service.ITopicService;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.exception.GeneralException;

/**
 * Service for {@link com.tx.co.back_office.company.domain.CompanyTopic}s.
 *
 * @author aazo
 */
@Service
public class CompanyTopicService extends UpdateCacheData implements ICompanyTopicService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(CompanyTopicService.class);
	
	private CompanyTopicRepository companyTopicRepository;
	private ITopicService topicService;
	
	@Autowired
	public void setCompanSyTopicRepository(CompanyTopicRepository companyTopicRepository) {   
		this.companyTopicRepository = companyTopicRepository;
	}

	@Autowired
	public void setTopicService(ITopicService topicService) {
		this.topicService = topicService;
	}

	/* (non-Javadoc)
	 * @see com.tx.co.back_office.company.service.ICompanyTopicService#getCompanyTopicByIdCompany(java.lang.String)
	 * 
	 * Retrieve Topic from cache in order to get the translations
	 */
	@Override
	public List<CompanyTopic> getCompanyTopicByIdCompany(String idCompany) {
		
		List<CompanyTopic> companyTopics = companyTopicRepository.getCompanyTopicByIdCompany(getCompanyById(Long.valueOf(idCompany)));
		
		logger.info("The number of the companyTopics: " + companyTopics.size());
		
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

	@Override
	public void deleteCompanyTopic(Long idCompanyTopic) {
		
		try {
    		logger.info("Deleting the Company Topic with id: " + idCompanyTopic);
    		
    		Optional<CompanyTopic> companyTopicOptional = companyTopicRepository.findById(idCompanyTopic);

    		if (!companyTopicOptional.isPresent()) {
    			throw new NotFoundException();
    		}

    		// The modification of User
    		String username = getTokenUserDetails().getUser().getUsername();

    		CompanyTopic companyTopic = companyTopicOptional.get();
    		// disable the company
    		companyTopic.setEnabled(false);
    		companyTopic.setModificationDate(new Date());
    		companyTopic.setModifiedBy(username);

    		companyTopicRepository.save(companyTopic);

    		if (!isEmpty(companyTopic.getTopic())) {
    			topicService.deleteTopic(companyTopic.getTopic().getIdTopic());
    		}
    		
    		logger.info("Deleting the Company Topic with id: " + idCompanyTopic );
    	} catch (Exception e) {
    		throw new GeneralException("Company Topic not found");
    	}
	}
	
}
