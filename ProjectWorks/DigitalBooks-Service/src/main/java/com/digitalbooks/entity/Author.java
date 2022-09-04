package com.digitalbooks.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int authorId;
	private String name;
	private String emailId;
	private String password;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "author")
	private List<Book> books=new ArrayList<>();
	
	public Author() {
		
	}
	public Author(String name, String emailId, String password) {
		this.name = name;
		this.emailId = emailId;
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
		
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		if(obj==null || this.getClass()!=obj.getClass()) {
			return false;
		}
		Author other=(Author) obj;
		return ((this.getAuthorId()==other.getAuthorId())&&(this.getName().equals(other.getName()))
				&&(this.getEmailId().equals(other.getEmailId()))&&(this.getPassword().equals(other.getPassword())));
	}
	@Override
	public int hashCode() {
		return this.authorId;
	}
	
	
	
}
