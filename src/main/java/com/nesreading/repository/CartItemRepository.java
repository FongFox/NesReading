package com.nesreading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.domain.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
