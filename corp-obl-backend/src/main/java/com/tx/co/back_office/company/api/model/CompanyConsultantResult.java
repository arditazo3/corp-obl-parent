package com.tx.co.back_office.company.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
* API model for returning company consultant details.
*
* @author aazo
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyConsultantResult {

	private Long idCompanyConsultant;
	private String name;
	private String email;
	private String phone1;
	private String phone2;
	private CompanyResult company;
	
	public Long getIdCompanyConsultant() {
		return idCompanyConsultant;
	}
	public void setIdCompanyConsultant(Long idCompanyConsultant) {
		this.idCompanyConsultant = idCompanyConsultant;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public CompanyResult getCompany() {
		return company;
	}
	public void setCompany(CompanyResult company) {
		this.company = company;
	}
	
	
}
