package com.nesreading.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "books")
@Getter
@Setter
@ToString
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Size(min = 1, message = "Title can not be blank.")
	private String title;

	@Column(name = "author", columnDefinition = "MEDIUMTEXT")
	@NotNull
	@Size(min = 1, message = "Author can not be blank.")
	private String author;

	@Column(name = "category")
	private String category;

	@NotNull
	@Size(min = 1, message = "Publisher can not be blank.")
	private String publisher;

	@Column(name = "publication_year")
	@Min(value = 1, message = "Publication Year must larger than 0")
	private int publicationYear;

	@DecimalMin(value = "0", inclusive = false, message = "Price must larger than 0")
	private double price;
	
	@Column(name = "short_description")
	@NotNull
	@Size(min = 1, message = "Short Description can not be blank.")
	private String shortDescription;
	
	@Column(name = "detail_description", columnDefinition = "MEDIUMTEXT")
	@NotNull
	@Size(min = 1, message = "Detail Description can not be blank.")
	private String detailDescription;

	@Min(value = 1, message = "Publication Year must larger than 0")
	private int stock;
	
	private int sold;
	
	private int status; //0: In stock; 1: Out of stock; 2: Not Available

	@OneToMany(mappedBy = "book")
	private List<BookReview> bookReviews;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "total_likes")
	private int totalLikes = 0;
	
	@CreationTimestamp
	@Column(name = "create_at")
	private LocalDateTime createAt;
	
	@UpdateTimestamp
	@Column(name = "update_at")
	private LocalDateTime updateAt;

	public Book() {
	}
}
