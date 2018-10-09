package com.tx.co.back_office.office.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.user.domain.User;

/**
 * Domain model that represents a office.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_office")
public class Office implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long idOffice;
	
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
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @OneToMany(mappedBy="task", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TaskOffice> taskOffices = new HashSet<>();

    @Transient
    private List<User> userProviders;
    
    @Transient
    private List<User> userBeneficiaries;
    
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<TaskOffice> getTaskOffices() {
		return taskOffices;
	}

	public void setTaskOffices(Set<TaskOffice> taskOffices) {
		this.taskOffices = taskOffices;
	}

	public List<User> getUserProviders() {
		return userProviders;
	}

	public void setUserProviders(List<User> userProviders) {
		this.userProviders = userProviders;
	}

	public List<User> getUserBeneficiaries() {
		return userBeneficiaries;
	}

	public void setUserBeneficiaries(List<User> userBeneficiaries) {
		this.userBeneficiaries = userBeneficiaries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idOffice == null) ? 0 : idOffice.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Office other = (Office) obj;
		if (idOffice == null) {
			if (other.idOffice != null)
				return false;
		} else if (!idOffice.equals(other.idOffice))
			return false;
		return true;
	}
}
