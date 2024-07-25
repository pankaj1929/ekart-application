package com.ekart.repository;



import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.CartProduct;
import org.springframework.stereotype.Repository;


@Repository
public interface CartProductRepository extends CrudRepository<CartProduct, Integer> {

}
