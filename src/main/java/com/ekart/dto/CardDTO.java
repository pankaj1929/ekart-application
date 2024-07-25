package com.ekart.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDTO {
	@Override
	public String toString() {
		return "CardDTO [cardType=" + cardType + ", cardNumber=" + cardNumber + ", nameOnCard=" + nameOnCard
				+ ", hashCvv=" + hashCvv + ", cvv=" + cvv + ", expiryDate=" + expiryDate + ", cardId=" + cardId
				+ ", customerEmailId=" + customerEmailId + "]";
	}
	private String cardType;
	private String cardNumber;
	private String nameOnCard;
	private String hashCvv;
	@NotNull(message = "{transaction.cvv.notpresent}")
	private Integer cvv;
	private LocalDate expiryDate;
	@NotNull(message = "{transaction.cardId.notpresent}")
	private Integer cardId;
	private String customerEmailId;

}
