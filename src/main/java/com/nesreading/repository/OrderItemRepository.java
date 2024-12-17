package com.nesreading.repository;

import com.nesreading.model.Book;
import com.nesreading.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
    OrderItem findByOrderAndBook(Order order, Book book);
}
