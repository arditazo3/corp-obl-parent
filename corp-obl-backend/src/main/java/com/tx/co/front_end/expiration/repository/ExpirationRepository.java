package com.tx.co.front_end.expiration.repository;

import org.springframework.data.repository.CrudRepository;

import com.tx.co.front_end.expiration.domain.Expiration;

public interface ExpirationRepository extends CrudRepository<Expiration, Long> {

}
