package com.tx.co.user.service;

import com.tx.co.user.domain.User;

import java.util.List;

public interface IUserService {

    List<User> findAllUsers();

    User findByUsername(String username);
    
    List<User> findAllUsersExceptRole(String role);
    
    List<String> getLang();
    
    List<String> getLangNotAvailable();
    
    void setUserRelationType(User user);
}
