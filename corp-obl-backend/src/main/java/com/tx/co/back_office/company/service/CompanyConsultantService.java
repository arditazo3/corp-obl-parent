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
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.exception.GeneralException;

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
        if(isEmpty(companyConsultant.getIdCompanyConsultant())) {
            companyConsultant.setCreationDate(new Date());
            companyConsultant.setCreatedBy(username);
            companyConsultant.setEnabled(true);
            companyConsultantStored = companyConsultant;
        } else { // Existing Company
            companyConsultantStored = getCompanyConsultantById(companyConsultant.getIdCompanyConsultant());
            companyConsultantStored.setName(companyConsultant.getName());
            companyConsultantStored.setEmail(companyConsultant.getEmail());
        }
        
        if(!isEmpty(companyConsultant.getCompany())) {
        	companyConsultantStored.setCompany(companyConsultant.getCompany());
        }
        if(!isEmpty(companyConsultant.getPhone1())) {
        	companyConsultantStored.setPhone1(companyConsultant.getPhone1());
        }
        if(!isEmpty(companyConsultant.getPhone2())) {
        	companyConsultantStored.setPhone2(companyConsultant.getPhone2());
        }

        companyConsultantStored.setModificationDate(new Date());
        companyConsultantStored.setModifiedBy(username);

        companyConsultantStored = companyConsultantRepository.save(companyConsultantStored);

        updateCompanyConsultantCache(companyConsultantStored, false);

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
    		Optional<CompanyConsultant> companyConsultantOptional = findByIdCompanyConsultant(idCompanyConsultant);

    		if(!companyConsultantOptional.isPresent()) {
    			throw new NotFoundException();
    		}

    		// The modification of User
    		String username = getTokenUserDetails().getUser().getUsername();

    		CompanyConsultant companyConsultant = companyConsultantOptional.get();
    		// disable the company
    		companyConsultant.setEnabled(false);
    		companyConsultant.setModificationDate(new Date());
    		companyConsultant.setModifiedBy(username);

    		companyConsultantRepository.save(companyConsultant);

    		updateCompanyConsultantCache(companyConsultant, false);
    	} catch (Exception e) {
    		throw new GeneralException("Company not found");
    	}
	}

	

}
