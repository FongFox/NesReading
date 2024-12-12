package com.nesreading.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "receiver_name")
	private String receiverName;
	
	@Column(name = "receiver_address")
	private String receiverAddress;
	
	@Column(name = "receiver_phone_number")
	private String receiverPhoneNumber;

	@Column(name = "order_note", columnDefinition = "MEDIUMTEXT")
	private String orderNote;
	
	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItems;

	@Column(name = "total_price")
	private double totalPrice;

	@Column(name = "sub_price")
	private double subPrice;

	@Column(name = "final_price")
	private double finalPrice;

	//0: Pending; 1: Shipping; 3: Delivered; 4: Completed; 5: Canceled
	private int status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@CreationTimestamp
	@Column(name = "create_at")
	private LocalDateTime createAt;
	
	@UpdateTimestamp
	@Column(name = "update_at")
	private LocalDateTime updateAt;

	public Order() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverPhoneNumber() {
		return receiverPhoneNumber;
	}

	public void setReceiverPhoneNumber(String receiverPhoneNumber) {
		this.receiverPhoneNumber = receiverPhoneNumber;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getSubPrice() {
		return subPrice;
	}

	public void setSubPrice(double subPrice) {
		this.subPrice = subPrice;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}

	public LocalDateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}

	public void handleCalculateSubAndFinalPrice(double totalPrice) {
		this.subPrice = totalPrice * 0.15;
		this.finalPrice = totalPrice + subPrice;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", receiverName='" + receiverName + '\'' +
				", receiverAddress='" + receiverAddress + '\'' +
				", receiverPhoneNumber='" + receiverPhoneNumber + '\'' +
				", orderNote='" + orderNote + '\'' +
				", orderItems=" + orderItems +
				", totalPrice=" + totalPrice +
				", subPrice=" + subPrice +
				", finalPrice=" + finalPrice +
				", status=" + status +
				", user=" + user +
				", createAt=" + createAt +
				", updateAt=" + updateAt +
				'}';
	}
}
