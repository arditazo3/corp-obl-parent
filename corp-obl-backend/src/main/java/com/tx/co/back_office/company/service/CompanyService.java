package com.tx.co.back_office.company.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.company.domain.CompanyUser;
import com.tx.co.back_office.company.repository.CompanyRepository;
import com.tx.co.back_office.company.repository.CompanyUserRespository;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.service.IOfficeService;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.user.domain.User;

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
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Service for {@link com.tx.co.back_office.company.domain.Company}s.
 *
 * @author aazo
 */
@Service
public class CompanyService extends UpdateCacheData implements ICompanyService, IUserManagementDetails {

    private static final Logger logger = LogManager.getLogger(CompanyService.class);

    private CompanyRepository companyRepository;
    private CompanyUserRespository companyUserRespository;
    private IOfficeService officeService;
    private ICompanyTopicService companyTopicService; 
    private ICompanyConsultantService companyConsultantService;

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

    @Autowired
	public void setCompanyUserRespository(CompanyUserRespository companyUserRespository) {
		this.companyUserRespository = companyUserRespository;
	}

    @Autowired
	public void setOfficeService(IOfficeService officeService) {
		this.officeService = officeService;
	}

    @Autowired
	public void setCompanyTopicService(ICompanyTopicService companyTopicService) {
		this.companyTopicService = companyTopicService;
	}

    @Autowired
	public void setCompanyConsultantService(ICompanyConsultantService companyConsultantService) {
		this.companyConsultantService = companyConsultantService;
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
            
            logger.info("Creating the new company");
        } else { // Existing Company
            companyStored = getCompanyById(company.getIdCompany());
            companyStored.setDescription(company.getDescription());
            
            logger.info("Updating the company with id: " + companyStored.getIdCompany());
        }

        companyStored.setModificationDate(new Date());
        companyStored.setModifiedBy(username);

        companyStored = companyRepository.save(companyStored);

        updateCompaniesCache(companyStored, false);

        logger.info("Stored the company with id: " + companyStored.getIdCompany());
        
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
    		logger.info("Deleting the Company with id: " + idCompany );
    		
    		Optional<Company> companyOptional = findByIdCompany(idCompany);

    		if(!companyOptional.isPresent()) {
    			throw new NotFoundException();
    		}

    		// The modification of User
    		String username = getTokenUserDetails().getUser().getUsername();

    		Company company = companyOptional.get();
    		// disable the company
    		company.setEnabled(false);
    		company.setModificationDate(new Date());
    		company.setModifiedBy(username);

    		companyRepository.save(company);

    		updateCompaniesCache(company, false);
    		
    		if(!isEmpty(company.getOffice())) {
    			for (Office office : company.getOffice()) {
					officeService.deleteOffice(office.getIdOffice());
				}
    		}
    		
    		if(!isEmpty(company.getCompanyConsultant())) {
    			for (CompanyConsultant companyConsultant : company.getCompanyConsultant()) {
    				companyConsultantService.deleteCompanyConsultant(companyConsultant.getIdCompanyConsultant());
				}
    		}
    		
    		if(!isEmpty(company.getCompanyUsers())) {
    			for (CompanyUser companyUser : company.getCompanyUsers()) {
    				deleteCompanyUser(companyUser);
				}
    		}
    		
    		if(!isEmpty(company.getCompanyTopic())) {
    			for (CompanyTopic companyTopic : company.getCompanyTopic()) {
    				companyTopicService.deleteCompanyTopic(companyTopic.getIdCompanyTopic());
				}
    		}
    		
    		logger.info("Delete the Company with id: " + idCompany );
    	} catch (Exception e) {
    		logger.error("Company not found", e);
    	}
    }
    
    public void deleteCompanyUser(CompanyUser companyUser) {
    	
    	try {
    		logger.info("Deleting the Company User with id: " + companyUser.getIdCompanyUser());
    		
    		// The modification of User
    		String username = getTokenUserDetails().getUser().getUsername();

    		// disable the company
    		companyUser.setEnabled(false);
    		companyUser.setModificationDate(new Date());
    		companyUser.setModifiedBy(username);

    		companyUserRespository.save(companyUser);

    		logger.info("Deleting the Company User with id: " + companyUser.getIdCompanyUser() );
    	} catch (Exception e) {
    		throw new GeneralException("Company User not found");
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
			
			logger.info("Associating user to company with id: " + company.getIdCompany() );
			
			List<String> userListIncluded = new ArrayList<>();
			// The modification of User
	        String username = getTokenUserDetails().getUser().getUsername();
			for (CompanyUser companyUser : company.getCompanyUsers()) {
				
				CompanyUser companyUserStored = null;
				
				Optional<CompanyUser> companyUserCheckIfExist =  companyUserRespository.getCompanyUserByUsernameAndCompanyId(companyUser.getUsername(), company);
				if(companyUserCheckIfExist.isPresent()) {
					companyUser.setIdCompanyUser(companyUserCheckIfExist.get().getIdCompanyUser());
				}
				
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
		        		throw new GeneralException("Row not found exception");
		        	}
		        }
				companyUserStored.setEnabled(true);
				companyUserStored.setModificationDate(new Date());
				companyUserStored.setModifiedBy(username);
				companyUserStored.setCompanyAdmin(companyUser.getCompanyAdmin());
				companyUserStored.setUsername(companyUser.getUsername());
				
				User userLoopFromCache = getUserFromUsername(companyUserStored.getUsername());
				if(!isEmpty(userLoopFromCache) &&
						!isEmpty(userLoopFromCache.getAuthorities()) &&
						(userLoopFromCache.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN) ||
								userLoopFromCache.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_INLAND))) {
					companyUserStored.setCompanyAdmin(true);
				}
				
				companyUserStored = companyUserRespository.save(companyUserStored);
				userListIncluded.add(companyUserStored.getUsername());
			}	
			companyUserRespository.updateCompanyUserNotEnable(company, userListIncluded);
			updateCompaniesCache(company, true);
		} else if(!isEmpty(company)) {
			
			Optional<Company> companyStoredOptional = findByIdCompany(company.getIdCompany());
			
			if(companyStoredOptional.isPresent()) {
				Company companyStored = companyStoredOptional.get();
				
				List<String> userListIncluded = companyStored.getCompanyUsers().stream().map(CompanyUser::getUsername).collect(Collectors.toList());
				
				if(!isEmpty(userListIncluded)) {
					companyUserRespository.updateCompanyUserEnable(companyStored, userListIncluded);	
				}
			}
		}
	}

	@Override
	public List<Company> getCompaniesByRole() {
		
		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();
		
		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			return companyRepository.findAllByOrderByDescriptionAsc();
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN) ||
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_INLAND)) {
			return companyRepository.getCompaniesByRole(username);
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER)) {
			return companyRepository.getCompaniesByRoleUser(username);
		}
		
		return new ArrayList<>();
	}

}
