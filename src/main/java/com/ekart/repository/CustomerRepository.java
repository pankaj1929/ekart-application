package com.ekart.repository;



import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

	String findByPhoneNumber(String phoneNumber);
	
    Customer findByEmailId(String emailId);
	// add methods if required

}