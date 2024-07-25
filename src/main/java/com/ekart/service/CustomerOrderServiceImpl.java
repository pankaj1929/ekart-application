package com.ekart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekart.dto.CustomerDTO;
import com.ekart.dto.OrderDTO;
import com.ekart.dto.OrderStatus;
import com.ekart.dto.OrderedProductDTO;
import com.ekart.dto.PaymentThrough;
import com.ekart.dto.ProductDTO;
import com.ekart.entity.Order;
import com.ekart.entity.OrderedProduct;
import com.ekart.exception.EKartException;
import com.ekart.repository.CustomerOrderRepository;

@Service(value = "customerOrderService")
@Transactional
public class CustomerOrderServiceImpl implements CustomerOrderService {

	@Autowired
	private CustomerOrderRepository orderRepository;

	@Autowired
	private CustomerService customerService;

	@Override
	public Integer placeOrder(OrderDTO orderDTO) throws EKartException {
		CustomerDTO customerDTO = customerService.getCustomerByEmailId(orderDTO.getCustomerEmailId());

		if (customerDTO.getAddress() == null || customerDTO.getAddress().isBlank()) {
			throw new EKartException("OrderService.ADDRESS_NOT_AVAILABLE");
		}

		Order order = new Order();
		order.setDeliveryAddress(customerDTO.getAddress());
		order.setCustomerEmailId(orderDTO.getCustomerEmailId());
		order.setDateOfDelivery(orderDTO.getDateOfDelivery());
		order.setDateOfOrder(LocalDateTime.now());
		order.setPaymentThrough(PaymentThrough.valueOf(orderDTO.getPaymentThrough()));
		order.setDiscount(order.getPaymentThrough() == PaymentThrough.CREDIT_CARD ? 10.0 : 5.0);
		order.setOrderStatus(OrderStatus.PLACED);

		double totalPrice = orderDTO.getOrderedProducts().stream()
				.mapToDouble(orderedProductDTO -> {
					if (orderedProductDTO.getProduct().getAvailableQuantity() < orderedProductDTO.getQuantity()) {
                        try {
                            throw new EKartException("OrderService.INSUFFICIENT_STOCK");
                        } catch (EKartException e) {
                            throw new RuntimeException(e);
                        }
                    }
					return orderedProductDTO.getQuantity() * orderedProductDTO.getProduct().getPrice();
				}).sum();

		List<OrderedProduct> orderedProducts = orderDTO.getOrderedProducts().stream()
				.map(this::toOrderedProduct)
				.collect(Collectors.toList());

		order.setOrderedProducts(orderedProducts);
		order.setTotalPrice(totalPrice * (100 - order.getDiscount()) / 100);

		orderRepository.save(order);

		return order.getOrderId();
	}

	@Override
	public OrderDTO getOrderDetails(Integer orderId) throws EKartException {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new EKartException("OrderService.ORDER_NOT_FOUND"));

		return toOrderDTO(order);
	}

	@Override
	public void updateOrderStatus(Integer orderId, OrderStatus orderStatus) throws EKartException {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new EKartException("OrderService.ORDER_NOT_FOUND"));
		order.setOrderStatus(orderStatus);
	}

	@Override
	public void updatePaymentThrough(Integer orderId, PaymentThrough paymentThrough) throws EKartException {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new EKartException("OrderService.ORDER_NOT_FOUND"));

		if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
			throw new EKartException("OrderService.TRANSACTION_ALREADY_DONE");
		}

		order.setPaymentThrough(paymentThrough);
	}

	@Override
	public List<OrderDTO> findOrdersByCustomerEmailId(String emailId) throws EKartException {
		List<Order> orders = orderRepository.findByCustomerEmailId(emailId);

		if (orders.isEmpty()) {
			throw new EKartException("OrderService.NO_ORDERS_FOUND");
		}

		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		try {
			List<CompletableFuture<OrderDTO>> futures = orders.stream()
					.map(order -> CompletableFuture.supplyAsync(() -> toOrderDTO(order), executorService))
					.toList();

			return futures.stream()
					.map(CompletableFuture::join)
					.collect(Collectors.toList());
		} finally {
			executorService.shutdown();
		}
	}

	private OrderedProduct toOrderedProduct(OrderedProductDTO orderedProductDTO) {
		OrderedProduct orderedProduct = new OrderedProduct();
		orderedProduct.setProductId(orderedProductDTO.getProduct().getProductId());
		orderedProduct.setQuantity(orderedProductDTO.getQuantity());
		return orderedProduct;
	}

	private OrderDTO toOrderDTO(Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setCustomerEmailId(order.getCustomerEmailId());
		orderDTO.setDateOfOrder(order.getDateOfOrder());
		orderDTO.setDiscount(order.getDiscount());
		orderDTO.setTotalPrice(order.getTotalPrice());
		orderDTO.setOrderStatus(order.getOrderStatus().toString());
		orderDTO.setPaymentThrough(order.getPaymentThrough().toString());
		orderDTO.setDateOfDelivery(order.getDateOfDelivery());
		orderDTO.setDeliveryAddress(order.getDeliveryAddress());

		List<OrderedProductDTO> orderedProductsDTOs = order.getOrderedProducts().stream()
				.map(this::toOrderedProductDTO)
				.collect(Collectors.toList());

		orderDTO.setOrderedProducts(orderedProductsDTOs);
		return orderDTO;
	}

	private OrderedProductDTO toOrderedProductDTO(OrderedProduct orderedProduct) {
		OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
		orderedProductDTO.setOrderedProductId(orderedProduct.getOrderedProductId());
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductId(orderedProduct.getProductId());
		orderedProductDTO.setProduct(productDTO);
		orderedProductDTO.setQuantity(orderedProduct.getQuantity());
		return orderedProductDTO;
	}
}
