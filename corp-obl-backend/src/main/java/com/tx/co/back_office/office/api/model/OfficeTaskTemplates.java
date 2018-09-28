package com.tx.co.back_office.office.api.model;

import java.util.ArrayList;
import java.util.List;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public class OfficeTaskTemplates {

	private Office office;
	private List<TaskTemplate> taskTemplates = new ArrayList<>();
	
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public List<TaskTemplate> getTaskTemplates() {
		return taskTemplates;
	}
	public void setTaskTemplates(List<TaskTemplate> taskTemplates) {
		this.taskTemplates = taskTemplates;
	}
}
