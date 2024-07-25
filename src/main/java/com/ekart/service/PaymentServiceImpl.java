package com.ekart.service;

import com.ekart.dto.CardDTO;
import com.ekart.dto.TransactionDTO;
import com.ekart.dto.TransactionStatus;
import com.ekart.entity.Card;
import com.ekart.entity.Transaction;
import com.ekart.exception.EKartException;
import com.ekart.repository.CardRepository;
import com.ekart.repository.TransactionRepository;
import com.ekart.utility.HashingUtility;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Add the missing annotation
@Service(value = "paymentService")
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Get the list of card details by using the customerEmailId
    // If the obtained list is empty throw EKartException with message
    // PaymentService.CUSTOMER_NOT_FOUND
    // Hash the card CVV value by calling the HashingUtility.getHashValue()
    // Populate the Card details and save to database
    // Return the cardId
    @Override
    public Integer addCustomerCard(String customerEmailId, CardDTO cardDTO)
            throws EKartException, NoSuchAlgorithmException {

        // write your logic here
        List<Card> cards = cardRepository.findByCustomerEmailId(customerEmailId);
        if (cards.isEmpty()) {
            throw new EKartException("paymentService.CUSTOMER_NOT_FOUND");

        }
        Card card = new Card();
        card.setCardID(cardDTO.getCardId());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardType(cardDTO.getCardType());
        card.setCustomerEmailId(cardDTO.getCustomerEmailId());
        card.setCvv(HashingUtility.getHashValue(cardDTO.getCvv().toString()));
        card.setExpiryDate(cardDTO.getExpiryDate());
        card.setNameOnCard(cardDTO.getNameOnCard());

        cards.add(card);
        cardRepository.saveAll(cards);


        return card.getCardID();
    }

    // Get the card details by using the given cardId(available in CardDTO)
    // If card not obtained throw an EKartException with message
    // PaymentService.CARD_NOT_FOUND
    // Else update the given card details using setters
    @Override
    public void updateCustomerCard(CardDTO cardDTO) throws EKartException, NoSuchAlgorithmException {

        // write your logic here
        Optional<Card> optional = cardRepository.findById(cardDTO.getCardId());
        Card card = optional.orElseThrow(() -> new EKartException("PaymentService.CARD_NOT_FOUND"));

        card.setCardID(cardDTO.getCardId());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardType(cardDTO.getCardType());
        card.setCustomerEmailId(cardDTO.getCustomerEmailId());
        card.setCvv(HashingUtility.getHashValue(cardDTO.getCvv().toString()));
        card.setExpiryDate(cardDTO.getExpiryDate());
        card.setNameOnCard(cardDTO.getNameOnCard());

    }

    // Get the list of card details by using the customerEmailId and cardId
    // If the obtained list is empty throw EKartException with message
    // PaymentService.CUSTOMER_NOT_FOUND
    // Else get the card detail and delete the same
    // If card not obtained throw an EKartException with message
    // PaymentService.CARD_NOT_FOUND
    @Override
    public void deleteCustomerCard(String customerEmailId, Integer cardId) throws EKartException {
        // write your logic here
        List<Card> cardList = cardRepository.findByCustomerEmailId(customerEmailId);
        if (cardList.isEmpty()) {
            throw new EKartException("PaymentService.CUSTOMER_NOT_FOUND");

        }

        Optional<Card> optional = cardRepository.findById(cardId);
        Card card = optional.orElseThrow(() -> new EKartException("PaymentService.CARD_NOT_FOUND"));
        for (Card c : cardList) {
            if (c.equals(card)) {
                cardRepository.delete(card);
                break;
            }
        }

    }

    // Get the card details by using the given cardId
    // If card not obtained throw an EKartException with message
    // PaymentService.CARD_NOT_FOUND
    // Else populate the card details and return

    @Override
    public CardDTO getCard(Integer cardId) throws EKartException {
        // write your logic here
        Optional<Card> optional = cardRepository.findById(cardId);

        Card card = optional.orElseThrow(() -> new EKartException("PaymentService.CARD_NOT_FOUND"));

        CardDTO cardDTO = new CardDTO();
		helperDto(cardDTO);
		cardDTO.setHashCvv(card.getCvv());
        return cardDTO;


    }

    // Get the list of card details by using the customerEmailId and cardType
    // If the obtained list is empty throw EKartException with message
    // PaymentService.CARD_NOT_FOUND
    // Else populate the obtained card details and return
    @Override
    public List<CardDTO> getCardsOfCustomer(String customerEmailId, String cardType) throws EKartException {

        // write your logic here
        List<Card> cardList = cardRepository.findByCustomerEmailIdAndCardType(customerEmailId, cardType);
        if (cardList.isEmpty()) {
            throw new EKartException("PaymentService.CARD_NOT_FOUND");

        }
        List<CardDTO> cardDTOs = new ArrayList<>();


        for (Card card : cardList) {
            CardDTO cardDTO = new CardDTO();
           helperDto(cardDTO);
			//cardDTO.setCvv(739);
			cardDTO.setHashCvv(card.getCvv());

            cardDTOs.add(cardDTO);


        }
        return cardDTOs;
    }

    @Override
    public Integer addTransaction(TransactionDTO transactionDTO) throws EKartException {
        if (transactionDTO.getTransactionStatus().equals(TransactionStatus.TRANSACTION_FAILED)) {
            throw new EKartException("PaymentService.TRANSACTION_FAILED_CVV_NOT_MATCHING");
        }
        Transaction transaction = new Transaction();
        transaction.setCardId(transactionDTO.getCard().getCardId());

        transaction.setOrderId(transactionDTO.getOrder().getOrderId());
        transaction.setTotalPrice(transactionDTO.getTotalPrice());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        transaction.setTransactionStatus(transactionDTO.getTransactionStatus());
        transactionRepository.save(transaction);

        return transaction.getTransactionId();
    }

    @Override
    public TransactionDTO authenticatePayment(String customerEmailId, TransactionDTO transactionDTO)
            throws EKartException, NoSuchAlgorithmException {
        if (!transactionDTO.getOrder().getCustomerEmailId().equals(customerEmailId)) {
            throw new EKartException("PaymentService.ORDER_DOES_NOT_BELONGS");

        }

        if (!transactionDTO.getOrder().getOrderStatus().equals("PLACED")) {
            throw new EKartException("PaymentService.TRANSACTION_ALREADY_DONE");

        }
        CardDTO cardDTO = getCard(transactionDTO.getCard().getCardId());
        if (!cardDTO.getCustomerEmailId().matches(customerEmailId)) {

            throw new EKartException("PaymentService.CARD_DOES_NOT_BELONGS");
        }
        if (!cardDTO.getCardType().equals(transactionDTO.getOrder().getPaymentThrough())) {

            throw new EKartException("PaymentService.PAYMENT_OPTION_SELECTED_NOT_MATCHING_CARD_TYPE");
        }
        if (cardDTO.getHashCvv().equals(HashingUtility.getHashValue(transactionDTO.getCard().getCvv().toString()))) {

            transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
        } else {

            transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_FAILED);

        }

        return transactionDTO;
    }

	private void helperDto(CardDTO cardDTO){
		cardDTO.setCardId(cardDTO.getCardId());
		cardDTO.setCardNumber(cardDTO.getCardNumber());
		cardDTO.setCardType(cardDTO.getCardType());
		cardDTO.setCustomerEmailId(cardDTO.getCustomerEmailId());
		cardDTO.setExpiryDate(cardDTO.getExpiryDate());
		cardDTO.setNameOnCard(cardDTO.getNameOnCard());
	}
}
