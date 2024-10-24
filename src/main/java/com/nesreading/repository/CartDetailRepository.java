package com.nesreading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.domain.CartItem;

public interface CartDetailRepository extends JpaRepository<CartItem, Integer> {

}
