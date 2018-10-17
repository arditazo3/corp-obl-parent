package com.tx.co.front_end.expiration.service;

import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.TaskTemplateExpirations;
import com.tx.co.front_end.expiration.domain.Expiration;

import java.util.List;

public interface IExpirationService {

    List<TaskTemplateExpirations> searchDateExpirationOffices(DateExpirationOfficesHasArchived dateExpirationOfficesHasArchived);

    /**
     * @param taskId
     * @return list of removable {@link Expiration}
     * @author rfratti
     */
    List<Expiration> getSchedulerRemovableExpirationList(long taskId);

    /**
     * @param expirationList
     * @return update list of {@link Expiration}
     * @author rfratti
     */
    boolean updateExpirationList(final List<Expiration> expirationList);
}
