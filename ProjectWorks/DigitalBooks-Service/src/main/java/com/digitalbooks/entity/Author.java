package com.digitalbooks.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int authorId;
	@NotBlank(message="Name should not be blank")
	private String name;
	@NotBlank(message="EmailId is mandatory")
	private String emailId;
	@NotBlank(message="Password is required")
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
