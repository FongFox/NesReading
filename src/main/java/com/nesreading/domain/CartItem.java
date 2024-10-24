package com.nesreading.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_item")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private long quantity;
  private double price;

  @ManyToOne()
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne()
  @JoinColumn(name = "book_id")
  private Book book;
}
