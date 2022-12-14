package com.digitalbooks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitalbooks.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
	public Author findByEmailId(String emailId);
}
