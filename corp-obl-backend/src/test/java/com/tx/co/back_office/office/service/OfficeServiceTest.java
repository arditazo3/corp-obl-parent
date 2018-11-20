package com.tx.co.back_office.office.service;

import static com.tx.co.common.constants.AppConstants.*;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tx.co.abstraction.AbstractServiceTest;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

/**
 * Tests for the office service class.
 *
 * @author aazo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfficeServiceTest extends AbstractServiceTest {

	@Test
	public void getOfficesAsAdmin() {
		
		setAuthorizationUsername(ADMIN);
		
		List<Office> offices = getOfficeService().getOfficesByRole();
		
		assertNotNull(offices);
	}
	
	@Test
	public void getOfficesAsForeign() {
		
		setAuthorizationUsername(FOREIGN);
		
		List<Office> offices = getOfficeService().getOfficesByRole();
		
		assertNotNull(offices);
	}
	
	@Test
	public void saveOffice_thenReturnOffice() {
		
		setAuthorizationUsername(ADMIN);
		
		List<Company> companies = getCompanyService().findAllCompany(); 
		
		assertNotNull(companies);
		
		Company company = companies.get(0);
		
		Office office = new Office();
		
		office.setDescription("Test office");
		office.setModificationDate(new Date());
		office.setModifiedBy(ADMIN);
		office.setCreationDate(new Date());
		office.setCreatedBy(ADMIN);
		office.setEnabled(true);
		
		office.setCompany(company);
		
		Office officeFound = getOfficeService().saveUpdateOffice(office);
		
		assertNotNull(officeFound.getIdOffice());
	}
}
