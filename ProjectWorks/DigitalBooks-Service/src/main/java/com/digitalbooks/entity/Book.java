package com.digitalbooks.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
	private float price;
	@NotBlank(message="Please enter category")
	private String category;
	private String authorName;
	@NotBlank(message="Please enter publisher details")
	private String publisher;
	private Date publishedDate;
	private String logo;
	private boolean active;
	@NotBlank(message="Please enter the content")
	private String chapter;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "author_id")
	private Author author;
	
	
}
