package com.tx.co.back_office.company.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.office.api.model.OfficeResult;

/**
 * API model for returning company details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResult {

    private Long idCompany;
    private String description;
    private List<CompanyUserResult> usersAssociated;
    private List<OfficeResult> offices;

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
	public List<CompanyUserResult> getUsersAssociated() {
		return usersAssociated;
	}

	public void setUsersAssociated(List<CompanyUserResult> usersAssociated) {
		this.usersAssociated = usersAssociated;
	}

	public List<OfficeResult> getOffices() {
		return offices;
	}

	public void setOffices(List<OfficeResult> offices) {
		this.offices = offices;
	}

	
}
