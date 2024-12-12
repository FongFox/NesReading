package com.nesreading.repository;

import com.nesreading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUser(User user);

    boolean existsByUser(User user);
}
