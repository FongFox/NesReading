package com.nesreading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.model.BookReview;

public interface BookReviewRepository extends JpaRepository<BookReview, Integer> {

}
