package com.tx.co.back_office.company.domain;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.topic.domain.Topic;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = false, name = "modificationdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Column(nullable = false, name = "modifiedby")
    private String modifiedBy;
    
    @OneToMany(mappedBy="company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Where(clause = "enabled = 1")
    private Set<CompanyUser> companyUsers = new HashSet<>();

    @OneToMany(mappedBy="company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Where(clause = "enabled = 1")
    private Set<Office> office = new HashSet<>();
    
    @OneToMany(mappedBy="company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Where(clause = "enabled = 1")
    private Set<CompanyTopic> companyTopic = new HashSet<>();
    
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

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Set<CompanyUser> getCompanyUsers() {
		return companyUsers;
	}

	public void setCompanyUsers(Set<CompanyUser> companyUsers) {
		this.companyUsers = companyUsers;
	}

	public Set<Office> getOffice() {
		return office;
	}

	public void setOffice(Set<Office> office) {
		this.office = office;
	}

	public Set<CompanyTopic> getCompanyTopic() {
		return companyTopic;
	}

	public void setCompanyTopic(Set<CompanyTopic> companyTopic) {
		this.companyTopic = companyTopic;
	}
}
