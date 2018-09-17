package com.tx.co.user.repository;

import com.tx.co.user.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link com.tx.co.user.domain.User}s.
 *
 * @author aazo
 */
public interface UserRepository extends CrudRepository<User, String> {

    @Query("select u from User u where u.username = ?1 and u.enabled = 1")
    User findByUsername(String username);
    
}