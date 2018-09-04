package com.tx.co.back_office.office.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.company.domain.Company;

/**
 * API model for returning office details.
 *
 * @author Ardit Azo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfficeResult {

    private Long idOffice;
    private String description;
    private String createdBy;
    private String modifiedBy;
    private Company company;
    
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
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
