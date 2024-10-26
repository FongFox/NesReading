package com.nesreading.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String title;
	
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;

	@Column(name = "other_authors", columnDefinition = "MEDIUMTEXT")
	private String otherAuthors;
	
	@ManyToOne
	@JoinColumn(name = "book_category_id")
	private BookCategory bookCategory;
	
	private double price;
	
	@Column(name = "short_description")
	private String shortDescription;
	
	@Column(name = "detail_description", columnDefinition = "MEDIUMTEXT")
	private String detailDescription;
	
	private int stock;
	
	private int sold;
	
	private int status; //0: In stock; 1: Out of stock; 2: Not Available

  @OneToMany(mappedBy = "book")
  private List<BookReview> bookReviews;
	
	@CreationTimestamp
	@Column(name = "create_at")
	private LocalDateTime createAt;
	
	@UpdateTimestamp
	@Column(name = "update_at")
	private LocalDateTime updateAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getOtherAuthors() {
		return otherAuthors;
	}

	public void setOtherAuthors(String otherAuthors) {
		this.otherAuthors = otherAuthors;
	}

	public BookCategory getBookCategory() {
		return bookCategory;
	}

	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<BookReview> getBookReviews() {
		return bookReviews;
	}

	public void setBookReviews(List<BookReview> bookReviews) {
		this.bookReviews = bookReviews;
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

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", otherAuthors=" + otherAuthors
				+ ", bookCategory=" + bookCategory + ", price=" + price + ", shortDescription=" + shortDescription
				+ ", detailDescription=" + detailDescription + ", stock=" + stock + ", sold=" + sold + ", status=" + status
				+ ", bookReviews=" + bookReviews + ", createAt=" + createAt + ", updateAt=" + updateAt + "]";
	}


}
