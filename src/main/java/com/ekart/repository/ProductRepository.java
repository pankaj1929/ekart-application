package com.ekart.repository;

import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

}
