package com.ekart.service.test;

import com.ekart.dto.CardDTO;
import com.ekart.dto.OrderDTO;
import com.ekart.dto.TransactionDTO;
import com.ekart.dto.TransactionStatus;
import com.ekart.entity.Card;
import com.ekart.exception.EKartException;
import com.ekart.repository.CardRepository;
import com.ekart.repository.TransactionRepository;
import com.ekart.service.PaymentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class CustomerCardsServiceTest {
    @InjectMocks
  private  PaymentServiceImpl paymentService;
    // Write testcases here
    @Mock
    private CardRepository cardRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void updateCustomerCardValidTest() throws EKartException {
        Card card = new Card();
        card.setCardID(122);
        card.setNameOnCard("AIM");
        card.setCvv("466");
        card.setCardType("6642150005012186");
        card.setCardNumber("0c658eb5d61e88c86f37613342bbce6cbf278a9a86ba6514dc7e5c205f76c99f");
        card.setExpiryDate(LocalDate.of(2028, 02, 24));
        //card.setExpiryDate("2028-02-24");
        card.setCustomerEmailId("tom@infosys.com");
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardId(100);
        cardDTO.setCvv(466);
        Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
        Assertions.assertDoesNotThrow(() -> paymentService.updateCustomerCard(cardDTO));
    }

    @Test
    void updateCustomerCardInValidTest() throws EKartException {
        CardDTO cardDTO = new CardDTO();
        Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        EKartException exp = Assertions.assertThrows(EKartException.class,
                () -> paymentService.updateCustomerCard(cardDTO));
        Assertions.assertEquals("PaymentService.CARD_NOT_FOUND", exp.getMessage());
    }


    @Test
    void getCardInValidTest() throws EKartException {
        Integer cardId = 100;
        Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Exception exp = Assertions.assertThrows(EKartException.class, () -> paymentService.getCard(cardId));
        Assertions.assertEquals("PaymentService.CARD_NOT_FOUND", exp.getMessage());
    }

    @Test
    void getCardValidTest() throws EKartException {
        Card card = new Card();
        card.setCardID(100);
        card.setCvv("466");
        card.setExpiryDate(LocalDate.of(2028, 2, 24));
        Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
        CardDTO cardDTO = paymentService.getCard(card.getCardID());
        Assertions.assertEquals(card.getCardID(), cardDTO.getCardId());
    }


    @Test
    void addTransactionInValidTest1() throws EKartException {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_FAILED);
        Exception exp = Assertions.assertThrows(EKartException.class,
                () -> paymentService.addTransaction(transactionDTO));
        Assertions.assertEquals("PaymentService.TRANSACTION_FAILED_CVV_NOT_MATCHING", exp.getMessage());
    }

    @Test
    void addTransactionInvalidTest1() throws EKartException {
        TransactionDTO transactionDTO = new TransactionDTO();
        CardDTO card = new CardDTO();
        card.setCardId(100);
        transactionDTO.setCard(card);
        OrderDTO order = new OrderDTO();
        order.setOrderId(111);
        transactionDTO.setOrder(order);
        transactionDTO.setTotalPrice(100.5d);
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setTransactionId(112);
        transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
        Assertions.assertDoesNotThrow(() -> paymentService.addTransaction(transactionDTO));
    }

    @Test
    void authenticatePaymentOnInValidTest() throws EKartException {
        String customerEmailId = "tom@infosys.com";
        TransactionDTO transactionDTO = new TransactionDTO();
        CardDTO card = new CardDTO();
        card.setCardId(100);
        transactionDTO.setCard(card);
        OrderDTO order = new OrderDTO();
        order.setOrderId(111);
        order.setCustomerEmailId("tom@infosys.com");
        order.setOrderStatus("CANCELLED");
        transactionDTO.setOrder(order);
        transactionDTO.setTotalPrice(100.5d);
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setTransactionId(112);
        transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
        Exception e = Assertions.assertThrows(EKartException.class,
                () -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
        Assertions.assertEquals("PaymentService.TRANSACTION_ALREADY_DONE", e.getMessage());
    }

    @Test
    void authenticatePaymentOnInValidTest1() throws EKartException {
        String customerEmailId = "tom@infosys.com";
        TransactionDTO transactionDTO = new TransactionDTO();
        CardDTO card = new CardDTO();
        card.setCardId(100);
        transactionDTO.setCard(card);
        OrderDTO order = new OrderDTO();
        order.setOrderId(111);
        order.setCustomerEmailId("tom@infosys.com");
        order.setOrderStatus("CANCELLED");
        transactionDTO.setOrder(order);
        transactionDTO.setTotalPrice(100.5d);
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setTransactionId(112);
        transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
        Exception e = Assertions.assertThrows(EKartException.class,
                () -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
        Assertions.assertEquals("PaymentService.TRANSACTION_ALREADY_DONE", e.getMessage());

    }

    @Test
    void authenticatePaymentInValidTest2() throws EKartException {
        String customerEmailId = "tom@gmail.com";
        TransactionDTO transactionDTO = getTransactionDTO();

        Card card = new Card();
        card.setCardType("CREDIT_CARD");
        card.setExpiryDate(LocalDate.of(2028, 02, 24));
        card.setNameOnCard("AIM");
        card.setCvv("466");
        card.setCardType("6642150005012186");
        card.setCardNumber("0c658eb5d61e88c86f37613342bbce6cbf278a9a86ba6514dc7e5c205f76c99f");
        card.setExpiryDate(LocalDate.of(2028, 02, 24));
        card.setCustomerEmailId("Customer@gmail.com");

        Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));

        Exception e = Assertions.assertThrows(EKartException.class,
                () -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
        Assertions.assertEquals("PaymentService.CARD_DOES_NOT_BELONGS", e.getMessage());

    }

    private static TransactionDTO getTransactionDTO() {
        TransactionDTO transactionDTO = new TransactionDTO();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomerEmailId("tom@infosys.com");
        orderDTO.setOrderStatus("PLACED");
        transactionDTO.setOrder(orderDTO);
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardId(1234);
        cardDTO.setCustomerEmailId("Customer@gmail.com");
        transactionDTO.setCard(cardDTO);
        cardDTO.setCustomerEmailId("Customer@gmail.com");
        transactionDTO.setCard(cardDTO);
        return transactionDTO;
    }
}
