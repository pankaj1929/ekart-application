package com.ekart.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;



@Setter
@Getter
public class TransactionDTO {
	@Override
	public String toString() {
		return "TransactionDTO [transactionId=" + transactionId + ", order=" + order + ", card=" + card
				+ ", totalPrice=" + totalPrice + ", transactionDate=" + transactionDate + ", transactionStatus="
				+ transactionStatus + "]";
	}
	private Integer transactionId;
	@Valid
	private OrderDTO order;
	@Valid
	private CardDTO card;
	private Double totalPrice;
	private LocalDateTime transactionDate;
	private TransactionStatus transactionStatus;


}
