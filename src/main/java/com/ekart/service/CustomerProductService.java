package com.ekart.service;

import java.util.List;

import com.ekart.dto.ProductDTO;
import com.ekart.exception.EKartException;


public interface CustomerProductService {
	List<ProductDTO> getAllProducts() throws EKartException;
	
    ProductDTO getProductById(Integer productId) throws EKartException;
	
	
    void reduceAvailableQuantity(Integer productId, Integer quantity) throws EKartException ;
}
