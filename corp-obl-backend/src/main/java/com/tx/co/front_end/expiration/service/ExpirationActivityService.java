package com.tx.co.front_end.expiration.service;

import static com.tx.co.common.constants.AppConstants.ADMIN;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;
import com.tx.co.front_end.expiration.repository.ExpirationActivityRepository;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.exception.GeneralException;

/**
 * Service for {@link com.tx.co.front_end.expiration.domain.ExpirationActivity}s.
 *
 * @author aazo
 */
@Service
public class ExpirationActivityService implements IExpirationActivityService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(ExpirationActivityService.class);
	
	private ExpirationActivityRepository expirationActivityRepository;
	
	@Autowired
	public void setExpirationActivityRepository(ExpirationActivityRepository expirationActivityRepository) {
		this.expirationActivityRepository = expirationActivityRepository;
	}

	@Override
	public ExpirationActivity saveUpdateExpirationActivity(Expiration expiration,
			ExpirationActivity expirationActivity) {

		// The modification of User
		String username = ADMIN;
		try {
			username = getTokenUserDetails().getUser().getUsername();
		} catch (Exception e) {
			logger.info("Job process of the ExpirationActivity");
		}

		ExpirationActivity expirationActivityStored = null;

		// New ExpirationActivity
		if (isEmpty(expirationActivity.getIdExpirationActivity())) {
			expirationActivity.setCreationDate(new Date());
			expirationActivity.setCreatedBy(username);
			expirationActivity.setDeleted(false);
			expirationActivityStored = expirationActivity;

			logger.info("Creating the new ExpirationActivity");
		} else { // Existing ExpirationActivity
			Optional<ExpirationActivity> expirationActivityOptional = expirationActivityRepository.findById(expirationActivity.getIdExpirationActivity());

			if (expirationActivityOptional.isPresent()) {
				expirationActivityStored = expirationActivityOptional.get();
			} else {
				throw new GeneralException("ExpirationActivity not found, id: " + expirationActivity.getIdExpirationActivity());
			}

			logger.info("Updating the expirationActivity with id: " + expirationActivity.getIdExpirationActivity());
		}
		
		expirationActivityStored.setExpiration(expiration);
		expirationActivityStored.setModificationDate(new Date());
		expirationActivityStored.setModifiedBy(username);
		expirationActivityStored.setExpirationActivityAttachments(expirationActivity.getExpirationActivityAttachments());
		
		expirationActivityStored = expirationActivityRepository.save(expirationActivityStored);
		
		return expirationActivityStored;
	}
	
	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
}
