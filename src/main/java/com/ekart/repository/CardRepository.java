package com.ekart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.Card;
import org.springframework.stereotype.Repository;


@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {
	
	// add methods if required
	List<Card> findByCustomerEmailId(String CustomerEmailId);
	Optional<Card> findByCardID(Integer cardID);
	List<Card> findByCustomerEmailIdAndCardType(String customerEmailId,String cardType);
}
