package com.nesreading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
