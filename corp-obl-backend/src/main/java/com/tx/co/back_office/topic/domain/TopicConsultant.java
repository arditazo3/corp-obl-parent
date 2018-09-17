package com.tx.co.back_office.topic.domain;

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

import com.tx.co.back_office.company.domain.CompanyConsultant;

/**
 * Domain model that represents a topic consultant.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_topicconsultant")
public class TopicConsultant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long idTopicConsultant;
	
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
	@JoinColumn(name = "topic_id")
	private Topic topic;
	
    @ManyToOne
	@JoinColumn(name = "consultantcompany_id")
	private CompanyConsultant companyConsultant;

	public Long getIdTopicConsultant() {
		return idTopicConsultant;
	}

	public void setIdTopicConsultant(Long idTopicConsultant) {
		this.idTopicConsultant = idTopicConsultant;
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

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public CompanyConsultant getCompanyConsultant() {
		return companyConsultant;
	}

	public void setCompanyConsultant(CompanyConsultant companyConsultant) {
		this.companyConsultant = companyConsultant;
	}
	
	
	
}
