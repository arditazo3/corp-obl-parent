package com.tx.co.user.service;

import com.tx.co.user.domain.User;

import java.util.List;

public interface IUserService {

    List<User> findAllUsers();

    User findByUsername(String username);
}
