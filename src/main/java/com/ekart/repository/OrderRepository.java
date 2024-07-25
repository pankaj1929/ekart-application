package com.ekart.repository;

import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
  
	// add methods if required
	
}
