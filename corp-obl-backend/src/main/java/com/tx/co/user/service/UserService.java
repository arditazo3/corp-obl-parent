package com.tx.co.user.service;

import static com.tx.co.common.constants.AppConstants.*;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.repository.CompanyRepository;
import com.tx.co.back_office.task.repository.TaskOfficeRelationRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.service.AuthenticationTokenService;
import com.tx.co.user.domain.User;
import com.tx.co.user.repository.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Service for {@link com.tx.co.user.domain.User}s.
 *
 * @author aazo
 */
@Service
public class UserService extends UpdateCacheData implements IUserService, IUserManagementDetails {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final TaskOfficeRelationRepository taskOfficeRelationRepository;
    private final AuthenticationTokenService authenticationTokenService;

    @Autowired
    public UserService(UserRepository userRepository, TaskOfficeRelationRepository taskOfficeRelationRepository, AuthenticationTokenService authenticationTokenService, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.taskOfficeRelationRepository = taskOfficeRelationRepository;
        this.authenticationTokenService = authenticationTokenService;
        this.companyRepository = companyRepository;
    }

    /**
     * @return get all the Users
     */
    @Override
    public List<User> findAllUsers() {
        List<User> userList = new ArrayList<>();

        List<User> userListFromCache = getUsersFromCache();
        if(!isEmpty(userListFromCache)) {
            userList = userListFromCache;
        } else {
            userRepository.findAll().forEach(userList::add);
        }

        logger.info("The number of the users: " + userList.size());

        return userList;
    }

    /**
     * @param username
     * @return get the user by username
     */
    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsersExceptRole(String role) {
    	List<User> userList = findAllUsers();
    	List<User> userListToRemove = new ArrayList<>();

    	for (User user : userList) {
    		if(!isEmpty(user.getAuthorities()) && 
    				user.getAuthorities().contains(Authority.valueOf(role))) {
    			userListToRemove.add(user);
    		}
    	}

    	userList.removeAll(userListToRemove);
    	return userList;
    }

	@Override
	public List<String> getLang() {
		return getLanguagesFromCache();
	}

	@Override
	public List<String> getLangNotAvailable() {
		return getLanguagesNotAvailableFromCache();
	}

	@Override
	public void setUserRelationType(User user) {
		if(!isEmpty(user) &&
				!isEmpty(user.getAuthorities()) && 
				user.getAuthorities().contains(Authority.CORPOBLIG_USER)) {

			String username = user.getUsername();
			
			if(!isEmpty(taskOfficeRelationRepository.
					getTaskOfficeRelationsByUsernameAndRelationType(username, CONTROLLER))) {
				user.getAuthorities().add(Authority.CORPOBLIG_CONTROLLER);
			}
			if(!isEmpty(taskOfficeRelationRepository.
					getTaskOfficeRelationsByUsernameAndRelationType(username, CONTROLLED))) {
				user.getAuthorities().add(Authority.CORPOBLIG_CONTROLLED);
			}
			
			checkIfUserIsCompanyAdmin(user);
		}
	}

	private void checkIfUserIsCompanyAdmin(User user) {

		List<Company> companies = companyRepository.getCompaniesByRoleUser(user.getUsername());
		
		if(!isEmpty(companies)) {
			user.getAuthorities().add(Authority.CORPOBLIG_USER_ADMIN_COMPANY);
		}
		
	}

	@Override
	public void userLanguangeChange(User user) {

		String username = user.getUsername();
		String lang = user.getLang().toUpperCase();
		
		User userChangeLangulage = findByUsername(username);
		userChangeLangulage.setLang(lang);
		
		userRepository.save(userChangeLangulage);
		
		updateUsersCache(userChangeLangulage, false);
		
		getTokenUserDetails().getUser().setLang(lang);

		authenticationTokenService.refreshToken(getTokenUserDetails());
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
}

