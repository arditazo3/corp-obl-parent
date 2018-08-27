package com.tx.co.back_office.company.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Domain model that represents a company.
 *
 * @author Ardit Azo
 */
@Entity
@Table(name = "co_company")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long idCompany;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false, name = "creationdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(nullable = false, name = "createdby")
    private String createdBy;

    @Column(nullable = false, name = "modifcationdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifcationDate;

    @Column(nullable = false, name = "modifiedby")
    private String modifiedBy;

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifcationDate() {
        return modifcationDate;
    }

    public void setModifcationDate(Date modifcationDate) {
        this.modifcationDate = modifcationDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (idCompany != null ? !idCompany.equals(company.idCompany) : company.idCompany != null) return false;
        if (description != null ? !description.equals(company.description) : company.description != null) return false;
        if (enabled != null ? !enabled.equals(company.enabled) : company.enabled != null) return false;
        if (creationDate != null ? !creationDate.equals(company.creationDate) : company.creationDate != null)
            return false;
        if (createdBy != null ? !createdBy.equals(company.createdBy) : company.createdBy != null) return false;
        if (modifcationDate != null ? !modifcationDate.equals(company.modifcationDate) : company.modifcationDate != null)
            return false;
        return modifiedBy != null ? modifiedBy.equals(company.modifiedBy) : company.modifiedBy == null;
    }

    @Override
    public int hashCode() {
        int result = idCompany != null ? idCompany.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (modifcationDate != null ? modifcationDate.hashCode() : 0);
        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
        return result;
    }
}
