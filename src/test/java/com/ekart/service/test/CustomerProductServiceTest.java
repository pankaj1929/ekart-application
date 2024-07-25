package com.ekart.service.test;

import com.ekart.entity.Product;
import com.ekart.exception.EKartException;
import com.ekart.repository.ProductRepository;
import com.ekart.service.CustomerProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private CustomerProductServiceImpl customerProductService;

	private Product product;

	@BeforeEach
	public void setUp() {
		product = new Product();
		product.setProductId(1);
		product.setAvailableQuantity(10);
	}

	@Test
	public void reduceAvailableQuantity_whenProductExists_shouldReduceQuantity() throws EKartException {
		// Given
		when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

		// When
		customerProductService.reduceAvailableQuantity(1, 5);

		// Then
		// Verify that the available quantity is reduced by 5
		assert product.getAvailableQuantity() == 5;
	}

	@Test
	public void reduceAvailableQuantity_whenProductDoesNotExist_shouldThrowEKartException() {
		// Given
		when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

		// When
		// Then
		// Verify that an EKartException is thrown when the product does not exist
		assertThrows(EKartException.class, () -> customerProductService.reduceAvailableQuantity(2, 5));
	}

	@Test
	public void reduceAvailableQuantity_whenQuantityIsMoreThanAvailable_shouldThrowEKartException() {
		// Given
		when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

		// When
		// Then
		// Verify that an EKartException is thrown when the quantity to reduce is more than available
		assertThrows(EKartException.class, () -> customerProductService.reduceAvailableQuantity(1, 15));
	}

	@Test
	public void reduceAvailableQuantity_whenQuantityIsNegative_shouldThrowEKartException() {
		// Given
		when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

		// When
		// Then
		// Verify that an EKartException is thrown when the quantity to reduce is negative
		assertThrows(EKartException.class, () -> customerProductService.reduceAvailableQuantity(1, -5));
	}
}