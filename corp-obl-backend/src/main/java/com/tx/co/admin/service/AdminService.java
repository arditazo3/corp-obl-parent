package com.tx.co.admin.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.cache.service.CacheDataLoader;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;

/**
 * Service for Admin
 *
 * @author aazo
 */
@Service
public class AdminService extends CacheDataLoader implements IAdminService, IUserManagementDetails {

	@Override
	public void refreshCache() {
		init();
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

}
