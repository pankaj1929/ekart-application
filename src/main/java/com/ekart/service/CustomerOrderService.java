package com.ekart.service;

import java.util.List;

import com.ekart.dto.OrderDTO;
import com.ekart.dto.OrderStatus;
import com.ekart.dto.PaymentThrough;
import com.ekart.exception.EKartException;



public interface CustomerOrderService {
	 Integer placeOrder(OrderDTO orderDTO) throws EKartException;
	 OrderDTO getOrderDetails (Integer orderId) throws EKartException;
	 List<OrderDTO> findOrdersByCustomerEmailId(String emailId) throws EKartException;
	 void updateOrderStatus( Integer orderId , OrderStatus orderStatus) throws EKartException;
	 void updatePaymentThrough( Integer orderId , PaymentThrough paymentThrough) throws EKartException;

}
