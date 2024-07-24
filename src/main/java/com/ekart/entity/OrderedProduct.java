package com.ekart.entity;

import jakarta.persistence.*;

@Entity
@Table(name="EK_ORDERED_PRODUCT")
public class OrderedProduct {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer orderedProductId;
	private Integer productId;
	private Integer quantity;
	
	
	public Integer getOrderedProductId() {
		return orderedProductId;
	}
	public void setOrderedProductId(Integer orderedProductId) {
		this.orderedProductId = orderedProductId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
