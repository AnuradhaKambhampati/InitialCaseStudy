package com.digitalbooks.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.digitalbooks.entity.Author;
import com.digitalbooks.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

	public List<Book> findByCategoryOrAuthorNameOrPriceOrPublisher(String category, String authorName, BigDecimal price, String publisher);
	
	public List<Book> findByAuthor(@Param(value = "author") Author author);
	
}
