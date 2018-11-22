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

import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.repository.CompanyConsultantRepository;
import com.tx.co.back_office.topic.domain.TopicConsultant;
import com.tx.co.back_office.topic.service.ITopicService;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.exception.GeneralException;

/**
 * Service for {@link com.tx.co.back_office.company.domain.CompanyConsultant}s.
 *
 * @author aazo
 */
@Service
public class CompanyConsultantService extends UpdateCacheData implements ICompanyConsultantService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(CompanyConsultantService.class);

	private CompanyConsultantRepository companyConsultantRepository;
	private ITopicService topicService;
	
	@Autowired
	public void setCompanyConsultantRepository(CompanyConsultantRepository companyConsultantRepository) {
		this.companyConsultantRepository = companyConsultantRepository;
	}

	@Autowired
	public void setTopicService(ITopicService topicService) {
		this.topicService = topicService;
	}

	@Override
	public List<CompanyConsultant> findAll() {
		return (List<CompanyConsultant>) companyConsultantRepository.findAll();
	}
	
	@Override
	public List<CompanyConsultant> getCompanyConsultantByIdCompany(String idCompany) {
		return companyConsultantRepository.getCompanyConsultantByIdCompany(getCompanyById(Long.valueOf(idCompany)));
	}

    /**
     * @param companyConsultant
     * @return the CompanyConsultant stored
     */
    @Override
    public CompanyConsultant saveUpdateCompanyConsultant(CompanyConsultant companyConsultant) {

        // The modification of User
        String username = getTokenUserDetails().getUser().getUsername();

        CompanyConsultant companyConsultantStored = null;

        // New CompanyConsultant
        if (isEmpty(companyConsultant.getIdCompanyConsultant())) {
            companyConsultant.setCreationDate(new Date());
            companyConsultant.setCreatedBy(username);
            companyConsultant.setEnabled(true);
            companyConsultantStored = companyConsultant;
            
            logger.info("Creating the new companyConsultant");
        } else { // Existing Company
            companyConsultantStored = getCompanyConsultantById(companyConsultant.getIdCompanyConsultant());
            companyConsultantStored.setName(companyConsultant.getName());
            companyConsultantStored.setEmail(companyConsultant.getEmail());
            
            logger.info("Updating the companyConsultant with id: " + companyConsultantStored.getIdCompanyConsultant());
        }
        
        if (!isEmpty(companyConsultant.getCompany())) {
        	companyConsultantStored.setCompany(companyConsultant.getCompany());
        }
        
        companyConsultantStored.setPhone1(companyConsultant.getPhone1());
        companyConsultantStored.setPhone2(companyConsultant.getPhone2());

        companyConsultantStored.setModificationDate(new Date());
        companyConsultantStored.setModifiedBy(username);

        companyConsultantStored = companyConsultantRepository.save(companyConsultantStored);

        updateCompanyConsultantCache(companyConsultantStored, false);

        logger.info("Stored the companyConsultant with id: " + companyConsultantStored.getIdCompanyConsultant());
        
        return companyConsultantStored;
    }
	
	
	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public Optional<CompanyConsultant> findByIdCompanyConsultant(Long idCompanyConsultant) {
		return companyConsultantRepository.findById(idCompanyConsultant);
	}

	@Override
	public void deleteCompanyConsultant(Long idCompanyConsultant) {
		try {
			logger.info("Deleting the companyConsultant with id: " + idCompanyConsultant );
			
    		Optional<CompanyConsultant> companyConsultantOptional = findByIdCompanyConsultant(idCompanyConsultant);

    		if (!companyConsultantOptional.isPresent()) {
    			throw new NotFoundException();
    		}

    		// The modification of User
    		String username = getTokenUserDetails().getUser().getUsername();

    		CompanyConsultant companyConsultant = companyConsultantOptional.get();
    		// disable the Company Consultant
    		companyConsultant.setEnabled(false);
    		companyConsultant.setModificationDate(new Date());
    		companyConsultant.setModifiedBy(username);

    		companyConsultantRepository.save(companyConsultant);
    		
    		for (TopicConsultant topicConsultant : companyConsultant.getTopicConsultants()) {
				topicService.deleteTopicConsultant(topicConsultant);
			}

    		updateCompanyConsultantCache(companyConsultant, false);
    		
    		logger.info("Delete the Company Consultant with id: " + idCompanyConsultant );
    	} catch (Exception e) {
    		throw new GeneralException("Company Consultant not found with id: " + idCompanyConsultant);
    	}
	}

	

}
