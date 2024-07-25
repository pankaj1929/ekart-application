package com.ekart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="EK_PRODUCT")
public class Product {
	
	@Id
	@Column(name="PRODUCT_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer productId;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="CATEGORY")
	private String category;
	
	@Column(name="BRAND")
	private String brand;
	
	@Column(name="PRICE")
	private Double price;

	
	@Column(name="QUANTITY")
	private Integer availableQuantity;


}