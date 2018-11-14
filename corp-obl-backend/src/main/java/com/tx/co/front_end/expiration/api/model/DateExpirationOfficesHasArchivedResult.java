package com.tx.co.front_end.expiration.api.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.office.api.model.OfficeResult;

/**
 * API model for returning date expiration offices has archived.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateExpirationOfficesHasArchivedResult {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateStart;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateEnd;
	private List<OfficeResult> offices;
	private Boolean showArchived;
	private Integer userRelationType;
	
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
	public List<OfficeResult> getOffices() {
		return offices;
	}
	public void setOffices(List<OfficeResult> offices) {
		this.offices = offices;
	}
	public Boolean getShowArchived() {
		return showArchived;
	}
	public void setShowArchived(Boolean showArchived) {
		this.showArchived = showArchived;
	}
	public Integer getUserRelationType() {
		return userRelationType;
	}
	public void setUserRelationType(Integer userRelationType) {
		this.userRelationType = userRelationType;
	}
}
