package com.tx.co.back_office.task.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import com.tx.co.front_end.expiration.domain.Expiration;

/**
 * Domain model that represents a task.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_task")
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long idTask;

	@ManyToOne
	@JoinColumn(name = "tasktemplate_id")
	private TaskTemplate taskTemplate;
	
	@OneToMany
	@JoinColumn (name = "task_id", insertable = false, updatable = false)
	@Fetch(value = FetchMode.JOIN)
//	@Where(clause = "enabled = 1")
    private Set<TaskOffice> taskOffices = new HashSet<>();
	
	@OneToMany
	@JoinColumn (name = "task_id", insertable = false, updatable = false)
	@Fetch(value = FetchMode.JOIN)
//	@Where(clause = "enabled = 1")
    private Set<Expiration> expirations = new HashSet<>();
	
	@Column(nullable = false)
	private String recurrence;

	@Column(nullable = false, name = "expirationtype")
	private String expirationType;

	@Column(nullable = false)
	private Integer day;

	@Column(nullable = false, name = "daysofnotice")
	private Integer daysOfNotice;
	
	@Column(nullable = false, name = "frequenceofnotice")
	private Integer frequenceOfNotice;

	@Column(nullable = false, name = "daysbeforeshowexpiration")
	private Integer daysBeforeShowExpiration;
	
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
	private List<Office> officesAssociated;
	
	@Transient
	private String descriptionTask;
	
	@Transient
	private Integer counterCompany;

	@Transient
	private Office office;
	
	@Transient
	private Boolean excludeOffice = false; 
	
	public Long getIdTask() {
		return idTask;
	}

	public void setIdTask(Long idTask) {
		this.idTask = idTask;
	}

	public TaskTemplate getTaskTemplate() {
		return taskTemplate;
	}

	public void setTaskTemplate(TaskTemplate taskTemplate) {
		this.taskTemplate = taskTemplate;
	}

	public Set<TaskOffice> getTaskOffices() {
		return taskOffices;
	}

	public void setTaskOffices(Set<TaskOffice> taskOffices) {
		this.taskOffices = taskOffices;
	}

	public Set<Expiration> getExpirations() {
		return expirations;
	}

	public void setExpirations(Set<Expiration> expirations) {
		this.expirations = expirations;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}

	public String getExpirationType() {
		return expirationType;
	}

	public void setExpirationType(String expirationType) {
		this.expirationType = expirationType;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getDaysOfNotice() {
		return daysOfNotice;
	}

	public void setDaysOfNotice(Integer daysOfNotice) {
		this.daysOfNotice = daysOfNotice;
	}

	public Integer getFrequenceOfNotice() {
		return frequenceOfNotice;
	}

	public void setFrequenceOfNotice(Integer frequenceOfNotice) {
		this.frequenceOfNotice = frequenceOfNotice;
	}

	public Integer getDaysBeforeShowExpiration() {
		return daysBeforeShowExpiration;
	}

	public void setDaysBeforeShowExpiration(Integer daysBeforeShowExpiration) {
		this.daysBeforeShowExpiration = daysBeforeShowExpiration;
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

	public List<Office> getOfficesAssociated() {
		return officesAssociated;
	}

	public void setOfficesAssociated(List<Office> officesAssociated) {
		this.officesAssociated = officesAssociated;
	}

	public String getDescriptionTask() {
		return descriptionTask;
	}

	public void setDescriptionTask(String descriptionTask) {
		this.descriptionTask = descriptionTask;
	}

	public Integer getCounterCompany() {
		return counterCompany;
	}

	public void setCounterCompany(Integer counterCompany) {
		this.counterCompany = counterCompany;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Boolean getExcludeOffice() {
		return excludeOffice;
	}

	public void setExcludeOffice(Boolean excludeOffice) {
		this.excludeOffice = excludeOffice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTask == null) ? 0 : idTask.hashCode());
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
		Task other = (Task) obj;
		if (idTask == null) {
			if (other.idTask != null)
				return false;
		} else if (!idTask.equals(other.idTask)) {
			return false;
		}
		return true;
	}
	
	
}
