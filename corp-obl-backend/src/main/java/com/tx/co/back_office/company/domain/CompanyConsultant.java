package com.tx.co.back_office.company.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Domain model that represents a company consultant.
 *
 * @author Ardit Azo
 */
@Entity
@Table(name = "co_companyconsultant")
public class CompanyConsultant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long idCompanyConsultant;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	private String phone1;
	private String phone2;

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
}
