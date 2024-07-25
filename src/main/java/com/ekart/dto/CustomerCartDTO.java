package com.ekart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;





@Setter
@Getter
public class CustomerCartDTO {
	
	private Integer cartId;
	@NotNull(message = "{customeremail.absent}")
	@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+" , message = "{invalid.customeremail.format}")
	private String customerEmailId;
	@Valid
	private Set<CartProductDTO> cartProducts;


}
