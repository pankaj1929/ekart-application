package com.ekart.repository;



import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.Transaction;



public interface TransactionRepository   extends CrudRepository<Transaction, Integer> {

}
