package com.tx.co.back_office.office.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.repository.OfficeRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;

/**
 * Service for {@link com.tx.co.back_office.company.domain.Offfice}s.
 *
 * @author Ardit Azo
 */
@Service
public class OfficeService extends UpdateCacheData implements IOfficeService, IUserManagementDetails  {

	private static final Logger logger = LogManager.getLogger(OfficeService.class);
	
	private OfficeRepository officeRepository;
	
	@Autowired
	public void setOfficeRepository(OfficeRepository officeRepository) {
		this.officeRepository = officeRepository;
	}

	/**
     * @return get all the Office
     */
    @Override
    public List<Office> findAllOffice() {
        List<Office> officeList = new ArrayList<>();

        List<Office> officeListFromCache = getOfficesFromCache();
        if(!isEmpty(getOfficesFromCache())) {
        	officeList = officeListFromCache;
        } else {
        	officeRepository.findAllByOrderByDescriptionAsc().forEach(officeList::add);
        }

        logger.info("The number of the offices: " + officeList.size());

        return officeList;
    }
	
	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public Office saveUpdateOffice(Office office) {
		// TODO Auto-generated method stub
		return null;
	}

}
