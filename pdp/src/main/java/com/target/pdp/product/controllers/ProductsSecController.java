package com.target.pdp.product.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.target.pdp.product.domain.ProductDetails;
import com.target.pdp.product.domain.ProductPrice;
import com.target.pdp.product.service.ProductServiceInterface;

import lombok.extern.slf4j.Slf4j;

/**
 * Products SECURED API for retrieving product and price details and update product price information
 * This API aggregate product data from redsky API and product price data store
 * 
 * @author KK
 *
 */

@RestController
@RequestMapping("/v2/products")
@Slf4j
public class ProductsSecController {
	
	@Autowired
	ProductServiceInterface productInteface;
	
	public ProductsSecController(ProductServiceInterface productInteface) {
		this.productInteface = productInteface;
	}

	/*
	 * HealthCheck for Products API
	 */
	
	@GetMapping(value="/apistatus",produces="application/json")
	public String status() {
		return "Success";
	}
	
	/*
	 * For given product Id, it fetches product name and prices details from different EPs and aggregates.
	 * 
	 * @param product Id.
	 * @return product and price details
	 * @throws ProductNotFound 
	 */
	@GetMapping(value="/{id}",produces="application/json")
	@ResponseBody
	public ProductDetails getProductDetails(@PathVariable Long id) {
		log.info("Getting product price for product : "+id);
		return  productInteface.getProductDetails(id);

	}
	
	
	/*
	 * For given Product Id and Price information, it updates the Product Price data store.
	 * 
	 * @param prouduct Id
	 * @param proudct price
	 * @return 
	 * @throws ProductPriceNotFound
	 * 
	 */
	@PutMapping(value="/{id}",consumes="application/json")
	@ResponseBody
	public void updateProductPrice(@PathVariable Long id,@RequestBody @Valid ProductPrice productPrice) {
		productPrice.setId(id);
		log.info("Saving Product Price : "+productPrice);
		productInteface.updateProductPrice(productPrice);
	}	
	
	

}
