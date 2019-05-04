package com.target.pdp.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.target.pdp.product.domain.ProductPrice;

import lombok.extern.slf4j.Slf4j;

/**
 * This class provides the unit testing coverage for Mongo data store API calls
 * 
 * @author KK
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductPriceRepositoryTests {
	
	static final Long proudctId = new Long(13860428);
	
	@Autowired
	private ProductPriceRepository repository;	
	


	@Before
	public void init() {
		repository.deleteAll();
		ProductPrice price = new ProductPrice(proudctId, "USD", 22.22);
		repository.save(price);
	}
	
	/*
	 * Tests the product price availability in the data store
	 */
	
	@Test
	public void findByIdTest() {
		ProductPrice price = repository.findById(proudctId).get();
		log.info(""+price.getValue());
		assertThat(price.getValue()).isEqualTo(22.22);
	}
}
