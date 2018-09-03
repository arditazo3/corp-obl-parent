package com.tx.co.back_office.company.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyUser;
import com.tx.co.back_office.company.repository.CompanyRepository;
import com.tx.co.back_office.company.repository.CompanyUserRespository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.exception.GeneralException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Service for {@link com.tx.co.back_office.company.domain.Company}s.
 *
 * @author Ardit Azo
 */
@Service
public class CompanyService extends UpdateCacheData implements ICompanyService, IUserManagementDetails {

    private static final Logger logger = LogManager.getLogger(CompanyService.class);

    private CompanyRepository companyRepository;
    private CompanyUserRespository companyUserRespository;

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

    @Autowired
	public void setCompanyUserRespository(CompanyUserRespository companyUserRespository) {
		this.companyUserRespository = companyUserRespository;
	}

	/**
     * @return get all the Companies
     */
    @Override
    public List<Company> findAllCompany() {
        List<Company> companyList = new ArrayList<>();

        List<Company> companyListFromCache = getCompaniesFromCache();
        if(!isEmpty(getCompaniesFromCache())) {
            companyList = companyListFromCache;
        } else {
            companyRepository.findAllByOrderByDescriptionAsc().forEach(companyList::add);
        }

        logger.info("The number of the companies: " + companyList.size());

        return companyList;
    }

    /**
     * @param company
     * @return the Company stored
     */
    @Override
    public Company saveUpdateCompany(Company company) {

        // Check if exist other company with same description
        if(checkIfExistOtherCompanySameDescription(company)) {
            throw new GeneralException("This company already exist");
        }

        // The modification of User
        String username = getTokenUserDetails().getUser().getUsername();

        Company companyStored = null;

        // New Company
        if(isEmpty(company.getIdCompany())) {
            company.setCreationDate(new Date());
            company.setCreatedBy(username);
            company.setEnabled(true);
            companyStored = company;
        } else { // Existing Company
            companyStored = getCompanyById(company.getIdCompany());
            companyStored.setDescription(company.getDescription());
        }

        companyStored.setModificationDate(new Date());
        companyStored.setModifiedBy(username);

        companyStored = companyRepository.save(companyStored);

        updateCompaniesCache(companyStored, false);

        return companyStored;
    }

    /**
     * @param company
     * @return true if the Company already exist
     */
    private boolean checkIfExistOtherCompanySameDescription(Company company) {

        List<Company> companyListByDescription = companyRepository.findCompaniesByDescription(company.getDescription());
        if (isEmpty(companyListByDescription)) {
            return false;
            // Check if i'm modifing the exist one
        } else {
            int counter = 0;
            for (Company companyLoop : companyListByDescription) {
                if ((!isEmpty(company.getIdCompany()) &&
                        companyLoop.getIdCompany().compareTo(company.getIdCompany()) != 0 &&
                        companyLoop.getDescription().trim().equalsIgnoreCase(company.getDescription().trim()))
                        ||
                        (isEmpty(company.getIdCompany()) &&
                                companyLoop.getDescription().trim().equalsIgnoreCase(company.getDescription().trim())) ) {
                    counter++;
                }
            }
            return counter > 0;
        }
    }

    /**
     * @param idCompany
     * @return the company
     */
    @Override
    public Optional<Company> findByIdCompany(Long idCompany) {
        return companyRepository.findById(idCompany);
    }

    @Override
    public void deleteCompany(Long idCompany) {

        try {
            Optional<Company> companyOptional = findByIdCompany(idCompany);

            if(!companyOptional.isPresent()) {
                throw new NotFoundException();
            }

            Company company = companyOptional.get();
            // disable the company
            company.setEnabled(false);

            companyRepository.save(company);

            updateCompaniesCache(company, false);
        } catch (Exception e) {
            throw new GeneralException("Company not found");
        }
    }


    @Override
    public AuthenticationTokenUserDetails getTokenUserDetails() {
        return (AuthenticationTokenUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

	@Override
	public void associateUserToCompany(Company company) {
		if(!isEmpty(company) && !isEmpty(company.getCompanyUsers())) {
			List<String> userListIncluded = new ArrayList<>();
			// The modification of User
	        String username = getTokenUserDetails().getUser().getUsername();
			for (CompanyUser companyUser : company.getCompanyUsers()) {
				
				CompanyUser companyUserStored = null;
				// New CompanyUser
		        if(isEmpty(companyUser.getIdCompanyUser())) {
		        	companyUser.setCreationDate(new Date());
		        	companyUser.setCreatedBy(username);
		        	companyUserStored = companyUser;
		        } else {
		        	Optional<CompanyUser> retrievedCompanyUser = companyUserRespository.findById(companyUser.getIdCompanyUser());
		        	
		        	if(retrievedCompanyUser.isPresent()) {
		        		companyUserStored = retrievedCompanyUser.get();
		        	} else {
		        		throw new GeneralException("Not row found exception");
		        	}
		        }
				companyUserStored.setEnabled(true);
				companyUserStored.setModificationDate(new Date());
				companyUserStored.setModifiedBy(username);
				companyUserStored.setCompanyAdmin(companyUser.getCompanyAdmin());
				
				companyUserStored = companyUserRespository.save(companyUserStored);
				userListIncluded.add(companyUserStored.getUsername());
			}	
			companyUserRespository.updateCompanyUserNotEnable(company, userListIncluded);
			updateCompaniesCache(company, true);
		}
	}

}
