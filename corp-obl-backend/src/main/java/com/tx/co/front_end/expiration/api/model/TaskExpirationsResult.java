package com.tx.co.front_end.expiration.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.task.api.model.TaskResult;

/**
 * API model for returning task expirations details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskExpirationsResult {

	private TaskResult task;
	private List<ExpirationResult> expirations;
	
	public List<ExpirationResult> getExpirations() {
		return expirations;
	}
	public void setExpirations(List<ExpirationResult> expirations) {
		this.expirations = expirations;
	}
	public TaskResult getTask() {
		return task;
	}
	public void setTask(TaskResult task) {
		this.task = task;
	}
}
