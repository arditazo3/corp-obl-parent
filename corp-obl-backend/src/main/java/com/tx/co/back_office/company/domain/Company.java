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

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
}
