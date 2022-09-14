package com.digitalbooks.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Reader {
	@Id
	@NotBlank(message="EmailId is mandatory")
	@Email(message = "Not a valid email")
	private String emailId;
	@NotBlank(message="Name should not be blank")
	private String name;
	@NotBlank(message="Password is required")
	@Size(min = 3,max = 8)
	private String password;
		
}
