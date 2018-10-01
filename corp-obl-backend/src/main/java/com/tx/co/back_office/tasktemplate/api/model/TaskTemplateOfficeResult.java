package com.tx.co.back_office.tasktemplate.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.office.api.model.OfficeResult;

/**
 * API model for returning task template office details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTemplateOfficeResult {

	private TaskTemplateResult taskTemplate;
	private OfficeResult office;
	
	public TaskTemplateResult getTaskTemplate() {
		return taskTemplate;
	}
	public void setTaskTemplate(TaskTemplateResult taskTemplate) {
		this.taskTemplate = taskTemplate;
	}
	public OfficeResult getOffice() {
		return office;
	}
	public void setOffice(OfficeResult office) {
		this.office = office;
	}
	
	
}
