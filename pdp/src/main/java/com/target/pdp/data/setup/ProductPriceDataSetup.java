package com.target.pdp.data.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.target.pdp.product.domain.ProductPrice;
import com.target.pdp.product.repository.ProductPriceRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * This class loads the initial dataset for proudct price details into productprice data store
 * 
 * @author KK
 *
 */
@Profile("devl")
@Component
@Slf4j
public class ProductPriceDataSetup implements CommandLineRunner {

	@Autowired
	private ProductPriceRepository repository;

	/*
	 * Initializing proudct price data store with sample dataset
	 */
	@Override
	public void run(String... args) throws Exception {
		repository.deleteAll();

		// save product prices
		repository.save(new ProductPrice(Long.valueOf(13860428), "USD",32.12));
		repository.save(new ProductPrice(Long.valueOf(15117729), "USD",42.22));
		repository.save(new ProductPrice(Long.valueOf(16483589), "USD",52.32));
		repository.save(new ProductPrice(Long.valueOf(16696652), "USD",62.42));
		repository.save(new ProductPrice(Long.valueOf(16752456), "USD",72.52));
		repository.save(new ProductPrice(Long.valueOf(15643793), "USD",82.62));

		// fetch all product prices
		log.info("Prices found with findAll():");
		log.info("-------------------------------");
		
		repository.findAll().stream().forEach(productPrice -> log.info(productPrice.toString()));

		// fetch an individual customer
		log.info("Product Price found with findById('13860428'):");
		log.info("--------------------------------");
		repository.findById(Long.valueOf(13860428)).ifPresent(productPrice -> log.info(productPrice.toString()));

	}
}
