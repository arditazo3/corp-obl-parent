package com.tx.co.user.service;

import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.domain.Authority;
import com.tx.co.user.domain.User;
import com.tx.co.user.repository.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Service for {@link com.tx.co.user.domain.User}s.
 *
 * @author Ardit Azo
 */
@Service
public class UserService extends UpdateCacheData implements IUserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        logger.info("The username " + username);

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
}

