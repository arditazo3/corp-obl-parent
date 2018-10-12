package com.tx.co.front_end.expiration.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

/**
 * Domain model that represents a expiration.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_expiration")
public class Expiration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long idExpiration;
	
	@ManyToOne
    @JoinColumn(name = "tasktemplate_id")
    private TaskTemplate taskTemplate;
	
	@ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
	
	@ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;
	
	@OneToMany
	@JoinColumn (name = "expiration_id", insertable = false, updatable = false)
	@Fetch(value = FetchMode.JOIN)
	@Where(clause = "deleted = 0")
    private Set<ExpirationActivity> expirationActivities = new HashSet<>();
	
	@Column(nullable = false, name="expirationclosableby")
    private String expirationClosableBy;
	
	@Column(nullable = false)
    private String username;
	
	@Column(nullable = false, name = "expirationdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date completed;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date approved;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registered;
	
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

	public Long getIdExpiration() {
		return idExpiration;
	}

	public void setIdExpiration(Long idExpiration) {
		this.idExpiration = idExpiration;
	}

	public TaskTemplate getTaskTemplate() {
		return taskTemplate;
	}

	public void setTaskTemplate(TaskTemplate taskTemplate) {
		this.taskTemplate = taskTemplate;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Set<ExpirationActivity> getExpirationActivities() {
		return expirationActivities;
	}

	public void setExpirationActivities(Set<ExpirationActivity> expirationActivities) {
		this.expirationActivities = expirationActivities;
	}

	public String getExpirationClosableBy() {
		return expirationClosableBy;
	}

	public void setExpirationClosableBy(String expirationClosableBy) {
		this.expirationClosableBy = expirationClosableBy;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getApproved() {
		return approved;
	}

	public void setApproved(Date approved) {
		this.approved = approved;
	}

	public Date getCompleted() {
		return completed;
	}

	public void setCompleted(Date completed) {
		this.completed = completed;
	}

	public Date getRegistered() {
		return registered;
	}

	public void setRegistered(Date registered) {
		this.registered = registered;
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
	
	
}
