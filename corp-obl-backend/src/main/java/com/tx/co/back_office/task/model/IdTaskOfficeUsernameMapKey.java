package com.tx.co.back_office.task.model;

import java.io.Serializable;

public class IdTaskOfficeUsernameMapKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idTaskOffice;
	private String username;
	
	public IdTaskOfficeUsernameMapKey(Long idTaskOffice, String username) {
		this.idTaskOffice = idTaskOffice;
		this.username = username;
	}
	public Long getIdTaskOffice() {
		return idTaskOffice;
	}
	public void setIdTaskOffice(Long idTaskOffice) {
		this.idTaskOffice = idTaskOffice;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTaskOffice == null) ? 0 : idTaskOffice.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		IdTaskOfficeUsernameMapKey other = (IdTaskOfficeUsernameMapKey) obj;
		if (idTaskOffice == null) {
			if (other.idTaskOffice != null)
				return false;
		} else if (!idTaskOffice.equals(other.idTaskOffice))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
	
	
}