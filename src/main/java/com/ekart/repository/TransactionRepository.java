package com.ekart.repository;



import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.Transaction;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository   extends CrudRepository<Transaction, Integer> {

}
