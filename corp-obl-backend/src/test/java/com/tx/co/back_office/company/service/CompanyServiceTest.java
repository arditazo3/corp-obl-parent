package com.tx.co.back_office.company.service;

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

/**
 * Tests for the company service class.
 *
 * @author aazo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanyServiceTest extends AbstractServiceTest {

	@Test
	public void getCompaniesAsAdmin() {
		
		setAuthorizationUsername(ADMIN);
		
		List<Company> companies = getCompanyService().getCompaniesByRole();
		
		assertNotNull(companies);
	}
	
	@Test
	public void getCompaniesAsInland() {
		
		setAuthorizationUsername(INLAND);
		
		List<Company> companies = getCompanyService().getCompaniesByRole();
		
		assertNotNull(companies);
	}
	
	@Test
	public void saveCompany_thenReturnCompany() {
		
		setAuthorizationUsername(FOREIGN);
		
		Company company = new Company();
		
		company.setDescription("Test company");
		company.setModifiedBy(FOREIGN);
		company.setCreationDate(new Date());
		company.setCreatedBy(FOREIGN);
		company.setEnabled(true);
		
		Company companyFound = getCompanyService().saveUpdateCompany(company);
		
		assertNotNull(companyFound.getIdCompany());
	}
}
