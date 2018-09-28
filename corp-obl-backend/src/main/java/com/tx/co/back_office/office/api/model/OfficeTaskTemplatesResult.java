package com.tx.co.back_office.office.api.model;

import java.util.List;

import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateResult;

public class OfficeTaskTemplatesResult {

	private OfficeResult office;
	private List<TaskTemplateResult> taskTemplates;
	
	public OfficeResult getOffice() {
		return office;
	}
	public void setOffice(OfficeResult office) {
		this.office = office;
	}
	public List<TaskTemplateResult> getTaskTemplates() {
		return taskTemplates;
	}
	public void setTaskTemplates(List<TaskTemplateResult> taskTemplates) {
		this.taskTemplates = taskTemplates;
	}
}
