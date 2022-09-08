package com.digitalbooks.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
public class Reader {
	@Id
	@NotBlank(message="EmailId is mandatory")
	private String emailId;
	@NotBlank(message="Name should not be blank")
	private String name;
	@NotBlank(message="Password is required")
	private String password;
		
}
