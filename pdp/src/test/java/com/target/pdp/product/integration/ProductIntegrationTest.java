package com.target.pdp.product.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.pdp.product.domain.ProductDetails;
import com.target.pdp.product.domain.ProductPrice;
import com.target.pdp.product.repository.ProductPriceRepository;

/**
 * This is a test class for integration test
 * The test cases covers end to end execution and asserts the response
 * 
 * @author KK
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

	static ProductPrice price;
	static ProductPrice priceForSave;
	static ProductPrice priceForReverification;
	static ProductPrice priceWithMissingCurrencyCode;
	static ProductPrice priceWithMissingPrice;
	static ProductDetails prodDetails; 
	static boolean setUpIsDone = false;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductPriceRepository repository;	
	
	@Autowired
	ObjectMapper objectMapper;
	
	@BeforeClass
	public static void setup() {
		
		price = new ProductPrice(Long.valueOf(13860428),"USD",22.22);
		prodDetails = new ProductDetails(Long.valueOf(13860428),"The Big Lebowski (Blu-ray)",price);
		priceForSave = new ProductPrice(null,"USD",22.22);
		priceForReverification = new ProductPrice(null,"USD",99.99);
		priceWithMissingCurrencyCode = new ProductPrice(null,"",22.22);
		priceWithMissingPrice = new ProductPrice(null,"USD",null);

	}
	
	@Before
	public void setUpPrice() {
	    if (setUpIsDone) {
	        return;
	    }
		repository.deleteAll();
		ProductPrice price = new ProductPrice(Long.valueOf(13860428), "USD", 22.22);
		ProductPrice priceForReverification = new ProductPrice(Long.valueOf(555555), "USD", 11.11);
		repository.save(price);		
		repository.save(priceForReverification);	
	    setUpIsDone = true;
	}
	
	/*
	 * Submits a get request and asserts the response for product details for a given produt id
	 */
	@Test
	public void testProductDetailsResponse() throws Exception {
		mockMvc.perform(get("/products/13860428")
			.contentType("application/json").accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", Matchers.is(13860428)))
				.andExpect(jsonPath("$.name",Matchers.is("The Big Lebowski (Blu-ray)")))
				.andExpect(jsonPath("$.current_price.value",Matchers.is(22.22)));
	}
	
	/*
	 * Submits a get request for missing product information and asserts the exception
	 */
	@Test
	public void testProductDetailsResponseForException() throws Exception {
		mockMvc.perform(get("/products/9999999")
			.contentType("application/json").accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());

	}
	
	/*
	 * Submit a put request for updating the product price and cross-verifies with the value stored in data store
	 */
	@Test
	public void testUpdateProductPrice() throws Exception {
		mockMvc.perform(put("/products/555555")
				.contentType("application/json").content(objectMapper.writeValueAsString(priceForReverification)))
					.andDo(print())
					.andExpect(status().isOk());
		ProductPrice productPrice = repository.findById(Long.valueOf(555555)).get();
		assertThat(productPrice.getValue()).isEqualTo(99.99);
	}

	/*
	 * Submits a put request for updating product price for missing product id in price data store and asserts for exception
	 */
	@Test
	public void testUpdateProductPriceForNotFound() throws Exception {
		mockMvc.perform(put("/products/777777")
				.contentType("application/json").content(objectMapper.writeValueAsString(priceForSave)))
					.andDo(print())
					.andExpect(status().isNotFound());

	}	

	/*
	 * Submits a put request with missing currency code and asserts the validation message
	 */
	@Test
	public void testSaveProductPriceForMissingCurrencyCode() throws Exception {
		mockMvc.perform(put("/products/13860428")
				.contentType("application/json").content(objectMapper.writeValueAsString(priceWithMissingCurrencyCode)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.message", Matchers.is("Product price currency Code is not supplied")));

	}	
	
	/*
	 * Submits a put request with missing price and asserts the validation message
	 */
	@Test
	public void testSaveProductPriceForMissingPrice() throws Exception {
		System.out.println("PriceMissing : "+objectMapper.writeValueAsString(priceWithMissingPrice));
		mockMvc.perform(put("/products/13860428")
				.contentType("application/json").content(objectMapper.writeValueAsString(priceWithMissingPrice)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.message", Matchers.is("Product price is not supplied")));

	}		
	
}
