package com.nesreading.repository;

import com.nesreading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    List<Order> findAllByUser(User user);
}
