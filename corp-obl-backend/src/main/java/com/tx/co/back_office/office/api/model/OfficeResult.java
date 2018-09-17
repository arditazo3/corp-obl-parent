package com.tx.co.back_office.office.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.company.api.model.CompanyResult;

/**
 * API model for returning office details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfficeResult {

    private Long idOffice;
    private String description;
    private CompanyResult company;
    
	public Long getIdOffice() {
		return idOffice;
	}
	public void setIdOffice(Long idOffice) {
		this.idOffice = idOffice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CompanyResult getCompany() {
		return company;
	}
	public void setCompany(CompanyResult company) {
		this.company = company;
	}
}
