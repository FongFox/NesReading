package com.nesreading.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.nesreading.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
  List<Book> findTop8ByOrderBySoldDesc();

  @Query(value = "SELECT * FROM books ORDER BY RAND() LIMIT 8", nativeQuery = true)
  List<Book> find8RandomBooks();

  Page<Book> findAll(Pageable pageable);

  @Query("SELECT b FROM Book b ORDER BY b.totalLikes DESC")
  List<Book> findTopLikedBooks(Pageable pageable);
}
