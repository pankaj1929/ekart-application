package com.ekart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekart.dto.ProductDTO;
import com.ekart.entity.Product;
import com.ekart.exception.EKartException;
import com.ekart.repository.ProductRepository;

//Add the missing annotation
@Service(value="customerProductService")
@Transactional
public class CustomerProductServiceImpl implements CustomerProductService {
	// This method will retrieve list of all the products from database

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<ProductDTO> getAllProducts() throws EKartException {
		// retrieving list of product from repository
		Iterable<Product> products = productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		products.forEach(product -> {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(product.getBrand());
			productDTO.setCategory(product.getCategory());
			productDTO.setDescription(product.getDescription());
			productDTO.setName(product.getName());
			productDTO.setPrice(product.getPrice());
			productDTO.setProductId(product.getProductId());
			productDTO.setAvailableQuantity(product.getAvailableQuantity());

			productDTOs.add(productDTO);
		});

		return productDTOs;
	}

	@Override
	public ProductDTO getProductById(Integer productId) throws EKartException {

		Optional<Product> productOp = productRepository.findById(productId);
		Product product = productOp.orElseThrow(() -> new EKartException("ProductService.PRODUCT_NOT_AVAILABLE"));

		ProductDTO productDTO = new ProductDTO();
		productDTO.setBrand(product.getBrand());
		productDTO.setCategory(product.getCategory());
		productDTO.setDescription(product.getDescription());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setProductId(product.getProductId());
		productDTO.setAvailableQuantity(product.getAvailableQuantity());

		return productDTO;
	}

	@Override
	public void reduceAvailableQuantity(Integer productId, Integer quantity) throws EKartException {
		Optional<Product> productOp = productRepository.findById(productId);
		Product product = productOp.orElseThrow(() -> new EKartException("ProductService.PRODUCT_NOT_AVAILABLE"));

		if (quantity <= 0) {
			throw new EKartException("ProductService.INVALID_QUANTITY");
		}

		if (quantity > product.getAvailableQuantity()) {
			throw new EKartException("ProductService.QUANTITY_EXCEEDS_AVAILABLE");
		}

		product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
	}
}