package com.tx.co.back_office.company.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning company user details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyUserResult {

	private Long idCompanyUser;
	private String username;
	private Boolean companyAdmin;
	private CompanyResult company;
	
	public Long getIdCompanyUser() {
		return idCompanyUser;
	}
	public void setIdCompanyUser(Long idCompanyUser) {
		this.idCompanyUser = idCompanyUser;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
