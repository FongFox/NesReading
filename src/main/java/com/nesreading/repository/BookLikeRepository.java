package com.nesreading.repository;

import com.nesreading.model.BookLike;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLikeRepository extends JpaRepository<BookLike, Integer> {
    boolean existsByUserIdAndBookId(int userId, int bookId);

    @Transactional
    void deleteByUserIdAndBookId(int userId, int bookId);
}
