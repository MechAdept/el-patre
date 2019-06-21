package com.samsolutions.repository;

import com.samsolutions.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findById(Integer id);
    User findByUsername(String username);
}
