package com.tx.co.front_end.expiration.service;

import static com.tx.co.common.constants.AppConstants.ADMIN;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tx.co.abstraction.AbstractServiceTest;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.TaskOfficeExpirations;
import com.tx.co.front_end.expiration.domain.Expiration;

/**
 * Tests for the expiration service class.
 *
 * @author aazo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExpirationServiceTest extends AbstractServiceTest {

	@Test
	public void getExpirationsAsAdmin() {
		
		setAuthorizationUsername(ADMIN); 
		
		Calendar calendar = Calendar.getInstance();
		
		DateExpirationOfficesHasArchived dateExpirationOfficesHasArchived = new DateExpirationOfficesHasArchived();
		
		calendar.set(2018, 0, 1);
		dateExpirationOfficesHasArchived.setDateStart(calendar.getTime());
		
		calendar.set(2018, 11, 31);
		dateExpirationOfficesHasArchived.setDateEnd(calendar.getTime());
		
		List<Office> offices = getOfficeService().findAllOffice();
		dateExpirationOfficesHasArchived.setOffices(offices);
		
		dateExpirationOfficesHasArchived.setShowArchived(true);
		
		List<TaskOfficeExpirations> taskTemplateExpirations = getExpirationService().searchDateExpirationOffices(dateExpirationOfficesHasArchived);
		
		assertNotNull(taskTemplateExpirations);
	}
	
	/**
	 * 
	 * @author rfratti
	 * */
	@Test
    public void getSchedulerRemovableExpirationList() {
        long taskId = 1;
        final Iterable<Expiration> expirationList = super.getExpirationService().getSchedulerRemovableExpirationList(taskId);
        assertNotNull(expirationList);
    }
}
