package com.tx.co.front_end.expiration.service;

import java.util.List;

import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.TaskTemplateExpirations;

public interface IExpirationService {

	List<TaskTemplateExpirations> searchDateExpirationOffices(DateExpirationOfficesHasArchived dateExpirationOfficesHasArchived);
}
