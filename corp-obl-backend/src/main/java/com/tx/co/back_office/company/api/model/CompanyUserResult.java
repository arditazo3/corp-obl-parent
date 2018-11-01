package com.tx.co.back_office.company.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.user.domain.User;

/**
 * API model for returning company user details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyUserResult {

	private Long idCompanyUser;
	private User user;
	private Boolean companyAdmin;
	private CompanyResult company;
	
	public Long getIdCompanyUser() {
		return idCompanyUser;
	}
	public void setIdCompanyUser(Long idCompanyUser) {
		this.idCompanyUser = idCompanyUser;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Boolean getCompanyAdmin() {
		return companyAdmin;
	}
	public void setCompanyAdmin(Boolean companyAdmin) {
		this.companyAdmin = companyAdmin;
	}
	public CompanyResult getCompany() {
		return company;
	}
	public void setCompany(CompanyResult company) {
		this.company = company;
	}
	
}
