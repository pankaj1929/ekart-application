package com.ekart.repository;

import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {
  
	// add methods if required
	
}
