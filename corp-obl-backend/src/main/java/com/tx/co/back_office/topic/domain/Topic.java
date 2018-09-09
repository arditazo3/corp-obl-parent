package com.tx.co.back_office.topic.domain;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.common.translation.domain.Translation;

/**
 * Domain model that represents a topic.
 *
 * @author Ardit Azo
 */
@Entity
@Table(name = "co_topic")
public class Topic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long idTopic;
	
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
    
    @OneToMany(mappedBy="topic", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Where(clause = "enabled = 1")
    private Set<CompanyTopic> companyTopic = new HashSet<>();

    @Transient
    private List<Translation> translationList = new ArrayList<>();
    
	public Long getIdTopic() {
		return idTopic;
	}

	public void setIdTopic(Long idTopic) {
		this.idTopic = idTopic;
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

	public Set<CompanyTopic> getCompanyTopic() {
		return companyTopic;
	}

	public void setCompanyTopic(Set<CompanyTopic> companyTopic) {
		this.companyTopic = companyTopic;
	}

	public List<Translation> getTranslationList() {
		return translationList;
	}

	public void setTranslationList(List<Translation> translationList) {
		this.translationList = translationList;
	}

}
