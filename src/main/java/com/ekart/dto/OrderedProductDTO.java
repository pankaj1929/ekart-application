package com.ekart.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderedProductDTO {

	
	private Integer orderedProductId;
	private ProductDTO product;
	private Integer quantity;


}
