package com.tx.co.back_office.company.domain;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public class OfficeTaskTemplate {

	private Office office;
	private TaskTemplate taskTemplate;
	
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public TaskTemplate getTaskTemplate() {
		return taskTemplate;
	}
	public void setTaskTemplate(TaskTemplate taskTemplate) {
		this.taskTemplate = taskTemplate;
	}
}
