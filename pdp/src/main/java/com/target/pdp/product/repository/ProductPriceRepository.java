package com.target.pdp.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.target.pdp.product.domain.ProductPrice;

/**
 * Repository interface for Product Price details
 * 
 * @author KK
 *
 */
public interface ProductPriceRepository extends MongoRepository<ProductPrice, Long> {

}
