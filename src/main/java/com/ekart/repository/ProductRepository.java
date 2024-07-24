package com.ekart.repository;

import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

}
