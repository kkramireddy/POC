package com.target.pdp.product.service;

import com.target.pdp.product.domain.ProductPrice;
import com.target.pdp.product.domain.ProductDetails;

/**
 * Products API Service Layer interface
 * 
 * @author KK
 *
 */
public interface ProductServiceInterface {

	/*
	 * Interacts with redsky API and product price data store.
	 * Aggregates details product and price details
	 * 
	 * @param product Id
	 * @throws ProductNotFound
	 */
	public ProductDetails getProductDetails(Long id);

	/*
	 * It updates the product price details to price data store only if product price details already present
	 * 
	 * @param proudct id and product price information
	 * @throws ProductPriceNotFound
	 */
	public void updateProductPrice(ProductPrice productPrice);



}
