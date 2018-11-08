package com.tx.co.back_office.task.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Domain model that represents a office task.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_taskofficerelations")
public class TaskOfficeRelations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long idTaskOfficeRelation;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "taskoffice_id")
    private TaskOffice taskOffice;
	
	@Column(nullable = false)
    private String username;
	
	@Column(name = "relationtype", nullable = false)
    private Integer relationType;
	
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

	public Long getIdTaskOfficeRelation() {
		return idTaskOfficeRelation;
	}

	public void setIdTaskOfficeRelation(Long idTaskOfficeRelation) {
		this.idTaskOfficeRelation = idTaskOfficeRelation;
	}

	public TaskOffice getTaskOffice() {
		return taskOffice;
	}

	public void setTaskOffice(TaskOffice taskOffice) {
		this.taskOffice = taskOffice;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getRelationType() {
		return relationType;
	}

	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
