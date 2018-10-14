package com.tx.co.front_end.expiration.api.model;

import java.util.Date;
import java.util.List;

import com.tx.co.back_office.office.domain.Office;

public class DateExpirationOfficesHasArchived {

	private Date dateStart;
	private Date dateEnd;
	private List<Office> offices;
	private Boolean hasArchived;
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public List<Office> getOffices() {
		return offices;
	}
	public void setOffices(List<Office> offices) {
		this.offices = offices;
	}
	public Boolean getHasArchived() {
		return hasArchived;
	}
	public void setHasArchived(Boolean hasArchived) {
		this.hasArchived = hasArchived;
	}
	
	
}
