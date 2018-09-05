package com.tx.co.back_office.company.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning company details.
 *
 * @author Ardit Azo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResult {

    private Long idCompany;
    private String description;
    private List<CompanyUserResult> usersAssociated;

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

	
}
