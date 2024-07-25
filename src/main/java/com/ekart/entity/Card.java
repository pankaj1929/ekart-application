package com.ekart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@Entity
@Table(name="EK_CARD")
public class Card {
	
	@Id
	@Column(name="CARD_ID")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer cardID;
	
	@Column(name="CARD_TYPE")
	private String cardType;
	
	@Column(name="CARD_NUMBER")
	private String cardNumber;
	
	@Column(name="CVV")
	private String cvv;
	
	@Column(name="EXPIRY_DATE")
	private LocalDate expiryDate;
	
	@Column(name="NAME_ON_CARD")
	private String nameOnCard;
	
	@Column(name="CUSTOMER_EMAIL_ID")
	private String customerEmailId;

}
