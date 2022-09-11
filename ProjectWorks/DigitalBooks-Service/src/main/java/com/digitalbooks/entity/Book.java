package com.digitalbooks.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message="Please provide title")
	private String title;
	@Min(value=1,message="Price should be >=1")
	//private float price;
	private BigDecimal price;
	@NotBlank(message="Please enter category")
	private String category;
	private String authorName;
	@NotBlank(message="Please enter publisher details")
	private String publisher;
	@PastOrPresent(message = "Date should be the past one")
	private Date publishedDate;
	private String logo;
	private boolean active;
	@Column(length = 1000)
	@NotBlank(message="Please enter the content")
	private String chapter;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "author_id")
	private Author author;
	
	
}
