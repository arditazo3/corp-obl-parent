package com.tx.co.front_end.expiration.service;

import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.TaskOfficeExpirations;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;

import java.util.List;

public interface IExpirationService {

    List<TaskOfficeExpirations> searchDateExpirationOffices(DateExpirationOfficesHasArchived dateExpirationOfficesHasArchived);

    Expiration saveUpdateExpiration(Expiration expiration);
    
    ExpirationActivity saveUpdateExpirationActivity(ExpirationActivity expirationActivity);
    
    /**
     * @param taskId
     * @return list of removable {@link Expiration}
     * @author rfratti
     */
    Iterable<Expiration> getSchedulerRemovableExpirationList(long taskId);

    /**
     * @param expirationList
     * @return update list of {@link Expiration}
     * @author rfratti
     */
    boolean updateExpirationList(final Iterable<Expiration> expirationList);
}
