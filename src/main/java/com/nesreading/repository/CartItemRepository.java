package com.nesreading.repository;

import com.nesreading.model.Book;
import com.nesreading.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByCartAndBook(Cart cart, Book book);

    boolean existsByCart(Cart cart);
}
