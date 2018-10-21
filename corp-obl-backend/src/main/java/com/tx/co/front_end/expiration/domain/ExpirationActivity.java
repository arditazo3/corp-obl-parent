package com.tx.co.front_end.expiration.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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

/**
 * Domain model that represents a expiration activity.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_expirationactivity")
public class ExpirationActivity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long idExpirationActivity;

	@ManyToOne
    @JoinColumn(name = "expiration_id")
    private Expiration expiration;
	
	@OneToMany(mappedBy="expirationActivity", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<ExpirationActivityAttachment> expirationActivityAttachments = new HashSet<>();
	
	@Column(nullable = false)
	private String body;
	
	@Column(nullable = false)
	private Boolean deleted;

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

	public Long getIdExpirationActivity() {
		return idExpirationActivity;
	}

	public void setIdExpirationActivity(Long idExpirationActivity) {
		this.idExpirationActivity = idExpirationActivity;
	}

	public Expiration getExpiration() {
		return expiration;
	}

	public void setExpiration(Expiration expiration) {
		this.expiration = expiration;
	}

	public Set<ExpirationActivityAttachment> getExpirationActivityAttachments() {
		return expirationActivityAttachments;
	}

	public void setExpirationActivityAttachments(Set<ExpirationActivityAttachment> expirationActivityAttachments) {
		this.expirationActivityAttachments = expirationActivityAttachments;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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
