package com.tx.co.front_end.expiration.service;

import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;

public interface IExpirationActivityService {

	ExpirationActivity saveUpdateExpirationActivity(Expiration expiration, ExpirationActivity expirationActivity);
}
