package com.ekart.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;


import com.ekart.dto.OrderStatus;
import com.ekart.dto.PaymentThrough;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "EK_ORDER")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;

	private String customerEmailId;

	private LocalDateTime dateOfOrder;

	private Double discount;

	private Double totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Enumerated(EnumType.STRING)
	private PaymentThrough paymentThrough;

	private LocalDateTime dateOfDelivery;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "orderId")
	private List<OrderedProduct> orderedProducts;
    
	private String deliveryAddress;

}
