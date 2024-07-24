package com.ekart.service;

import java.util.Set;

import com.ekart.dto.CartProductDTO;
import com.ekart.dto.CustomerCartDTO;
import com.ekart.exception.EKartException;

public interface CustomerCartService {

	Integer addProductToCart(CustomerCartDTO customerCart) throws EKartException;

	Set<CartProductDTO> getProductsFromCart(String customerEmailId) throws EKartException;

	void modifyQuantityOfProductInCart(String customerEmailId, Integer productId, Integer quantity)
			throws EKartException;

	void deleteProductFromCart(String customerEmailId, Integer productId) throws EKartException;

	void deleteAllProductsFromCart(String customerEmailId) throws EKartException;

}
