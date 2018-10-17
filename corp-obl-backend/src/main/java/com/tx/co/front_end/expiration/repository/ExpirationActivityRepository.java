package com.tx.co.front_end.expiration.repository;

import org.springframework.data.repository.CrudRepository;

import com.tx.co.front_end.expiration.domain.ExpirationActivity;

public interface ExpirationActivityRepository extends CrudRepository<ExpirationActivity, Long> {

}
