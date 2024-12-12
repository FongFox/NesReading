package com.nesreading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
