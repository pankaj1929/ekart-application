package com.ekart.service.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import com.ekart.dto.*;
import com.ekart.entity.Order;
import com.ekart.entity.OrderedProduct;
import com.ekart.exception.EKartException;
import com.ekart.repository.CustomerOrderRepository;
import com.ekart.service.CustomerOrderServiceImpl;
import com.ekart.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerOrderServiceImplTest {

    @Mock
    private CustomerOrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerOrderServiceImpl customerOrderService;

    private OrderDTO orderDTO;
    private CustomerDTO customerDTO;
    private Order order;

    @BeforeEach
    public void setUp() {
        customerDTO = new CustomerDTO();
        customerDTO.setAddress("123 Street");
        customerDTO.setEmailId("test@example.com");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1);
        productDTO.setPrice(100.0);
        productDTO.setAvailableQuantity(10);

        OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
        orderedProductDTO.setProduct(productDTO);
        orderedProductDTO.setQuantity(2);

        orderDTO = new OrderDTO();
        orderDTO.setCustomerEmailId("test@example.com");
        orderDTO.setDateOfDelivery(LocalDateTime.now().plusDays(3));
        orderDTO.setPaymentThrough("CREDIT_CARD");
        orderDTO.setOrderedProducts(Collections.singletonList(orderedProductDTO));

        order = new Order();
        order.setOrderId(1);
        order.setCustomerEmailId("test@example.com");
        order.setDeliveryAddress("123 Street");
        order.setDateOfDelivery(LocalDateTime.now().plusDays(3));
        order.setDateOfOrder(LocalDateTime.now());
        order.setPaymentThrough(PaymentThrough.CREDIT_CARD);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setDiscount(10.0);
        order.setTotalPrice(180.0);
    }

    @Test
    public void testPlaceOrder() throws EKartException {
        when(customerService.getCustomerByEmailId(anyString())).thenReturn(customerDTO);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Integer orderId = customerOrderService.placeOrder(orderDTO);

        assertNotNull(orderId);
        assertEquals(1, orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testPlaceOrderInsufficientStock() throws EKartException {
        // Set up the customer service to return a valid customer
        when(customerService.getCustomerByEmailId(anyString())).thenReturn(customerDTO);

        // Mock orderRepository to do nothing on save, as it shouldn't be called due to exception
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Set the available quantity to be less than the required quantity
        orderDTO.getOrderedProducts().get(0).getProduct().setAvailableQuantity(1);

        EKartException exception = assertThrows(EKartException.class, () -> customerOrderService.placeOrder(orderDTO));

        assertEquals("OrderService.INSUFFICIENT_STOCK", exception.getMessage());
    }

    @Test
    public void testGetOrderDetails() throws EKartException {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        OrderDTO result = customerOrderService.getOrderDetails(1);

        assertNotNull(result);
        assertEquals(order.getOrderId(), result.getOrderId());
        verify(orderRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testGetOrderDetailsOrderNotFound() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        EKartException exception = assertThrows(EKartException.class, () -> customerOrderService.getOrderDetails(1));

        assertEquals("OrderService.ORDER_NOT_FOUND", exception.getMessage());
    }

    @Test
    public void testUpdateOrderStatus() throws EKartException {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        customerOrderService.updateOrderStatus(1, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, order.getOrderStatus());
        verify(orderRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testUpdatePaymentThrough() throws EKartException {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        customerOrderService.updatePaymentThrough(1, PaymentThrough.DEBIT_CARD);

        assertEquals(PaymentThrough.DEBIT_CARD, order.getPaymentThrough());
        verify(orderRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testUpdatePaymentThroughAlreadyConfirmed() {
        order.setOrderStatus(OrderStatus.CONFIRMED);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        EKartException exception = assertThrows(EKartException.class, () -> customerOrderService.updatePaymentThrough(1, PaymentThrough.DEBIT_CARD));

        assertEquals("OrderService.TRANSACTION_ALREADY_DONE", exception.getMessage());
    }

    @Test
    public void testFindOrdersByCustomerEmailId() throws EKartException {
        when(orderRepository.findByCustomerEmailId(anyString())).thenReturn(Collections.singletonList(order));

        List<OrderDTO> orders = customerOrderService.findOrdersByCustomerEmailId("test@example.com");

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findByCustomerEmailId(anyString());
    }

    @Test
    public void testFindOrdersByCustomerEmailIdNoOrdersFound() {
        when(orderRepository.findByCustomerEmailId(anyString())).thenReturn(Collections.emptyList());

        EKartException exception = assertThrows(EKartException.class, () -> customerOrderService.findOrdersByCustomerEmailId("test@example.com"));

        assertEquals("OrderService.NO_ORDERS_FOUND", exception.getMessage());
    }
}
