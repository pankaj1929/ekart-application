package com.ekart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "EK_CUSTOMER")
public class Customer {
	@Id
	private String emailId;

	private String name;

	private String password;

	private String phoneNumber;

	private String address;

}
