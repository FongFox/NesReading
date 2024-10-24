package com.nesreading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nesreading.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
	Optional<Author> findByFullName(String fullname);
}
