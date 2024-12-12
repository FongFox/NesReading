package com.nesreading.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@Column(name = "book_price")
	private double bookPrice;

	@Column(name = "book_quantity")
	private int quantity;

	@Column(name = "total_price")
	private double totalPrice;

	public OrderItem() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public double getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(double bookPrice) {
		this.bookPrice = bookPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double calculateTotalPrice() {
		return this.bookPrice * this.quantity;
	}

	@Override
	public String toString() {
		return "OrderItem{" +
				"id=" + id +
				", order=" + order +
				", book=" + book +
				", bookPrice=" + bookPrice +
				", quantity=" + quantity +
				", totalPrice=" + totalPrice +
				'}';
	}
}
