package com.ekart.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.ekart.dto.TransactionStatus;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name="EK_TRANSACTION")
public class Transaction {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer transactionId;
	private Integer orderId;
	private Integer cardId;
	private Double totalPrice;
	private LocalDateTime transactionDate;
	@Enumerated(EnumType.STRING)
	private TransactionStatus transactionStatus;

}
