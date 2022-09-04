package com.digitalbooks.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Reader {
	@Id
	private String emailId;
	private String name;
	private String password;
	
	//private List<Book> readerBooks;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
//	public List<Book> getReaderBooks() {
//		return readerBooks;
//	}
//	public void setReaderBooks(List<Book> readerBooks) {
//		this.readerBooks = readerBooks;
//	}
	
	
}
