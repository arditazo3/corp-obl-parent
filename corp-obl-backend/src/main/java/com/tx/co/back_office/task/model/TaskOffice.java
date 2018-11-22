package com.tx.co.back_office.task.model;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

/**
 * Domain model that represents a office task.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_taskoffice")
public class TaskOffice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long idTaskOffice;
	
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
	@JoinColumn (name = "taskoffice_id", insertable = false, updatable = false)
	@Fetch(value = FetchMode.JOIN)
	@Where(clause = "enabled = 1")
    private Set<TaskOfficeRelations> taskOfficeRelations = new HashSet<>();
	
	@Column(nullable = false, name = "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
	
	@Column(nullable = false, name = "enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
	
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
    
    @Transient
    private Boolean isChangedObject = false;

	public Long getIdTaskOffice() {
		return idTaskOffice;
	}

	public void setIdTaskOffice(Long idTaskOffice) {
		this.idTaskOffice = idTaskOffice;
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

	public Set<TaskOfficeRelations> getTaskOfficeRelations() {
		return taskOfficeRelations;
	}

	public void setTaskOfficeRelations(Set<TaskOfficeRelations> taskOfficeRelations) {
		this.taskOfficeRelations = taskOfficeRelations;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTaskOffice == null) ? 0 : idTaskOffice.hashCode());
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
		TaskOffice other = (TaskOffice) obj;
		if (idTaskOffice == null) {
			if (other.idTaskOffice != null)
				return false;
			return false;
		} else if (!idTaskOffice.equals(other.idTaskOffice)) {
			return false;
		}
		return true;
	}
}
