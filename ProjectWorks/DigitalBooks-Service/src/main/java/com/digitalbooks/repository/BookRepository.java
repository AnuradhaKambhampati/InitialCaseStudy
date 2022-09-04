package com.digitalbooks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitalbooks.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

	public List<Book> findByCategoryOrAuthorNameOrPriceOrPublisher(String category, String authorName, float price, String publisher);

	
}
