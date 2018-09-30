package com.tx.co.back_office.task.api.model;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API model for returning task office relations details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskOfficeRelationsResult {

    private Long idTaskOfficeRelation;
    private TaskOfficeResult taskOffice;
    private String username;
    private Integer relationType;
    
	public Long getIdTaskOfficeRelation() {
		return idTaskOfficeRelation;
	}
	public void setIdTaskOfficeRelation(Long idTaskOfficeRelation) {
		this.idTaskOfficeRelation = idTaskOfficeRelation;
	}
	public TaskOfficeResult getTaskOffice() {
		return taskOffice;
	}
	public void setTaskOffice(TaskOfficeResult taskOffice) {
		this.taskOffice = taskOffice;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getRelationType() {
		return relationType;
	}
	public void setRelationType(Integer relationType) {
		this.relationType = relationType;
	}
    
    
}
