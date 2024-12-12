package com.nesreading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

}
