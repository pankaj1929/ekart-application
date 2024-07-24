package com.ekart.repository;



import org.springframework.data.repository.CrudRepository;

import com.ekart.entity.CartProduct;




public interface CartProductRepository extends CrudRepository<CartProduct, Integer> {

}
