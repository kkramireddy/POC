package com.target.pdp.product.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.pdp.product.domain.ProductDetails;
import com.target.pdp.product.domain.ProductPrice;
import com.target.pdp.product.exception.ProductNotFound;
import com.target.pdp.product.service.ProductServiceInterface;

import lombok.extern.slf4j.Slf4j;

/**
 * Junit Test class for testing Products API controller
 * 
 * @author KK
 *
 */

@RunWith(SpringRunner.class)
@WebMvcTest(ProductsController.class)
@Slf4j
public class ProductsControllerTests {
	
	static ProductPrice price;
	static ProductPrice priceForSave;
	static ProductPrice priceWithMissingCurrencyCode;
	static ProductPrice priceWithMissingPrice;
	static ProductDetails prodDetails;
	
	@MockBean
	ProductServiceInterface productInteface;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	

	 
	@BeforeClass
	public static void setup() {
		price = new ProductPrice(Long.valueOf(13860428),"USD",22.22);
		prodDetails = new ProductDetails(Long.valueOf(13860428),"The Big Lebowski (Blu-ray)",price);
		priceForSave = new ProductPrice(null,"USD",22.22);
		priceWithMissingCurrencyCode = new ProductPrice(null,"",22.22);
		priceWithMissingPrice = new ProductPrice(null,"USD",null);
	}

	/*
	 * Triggers a get request to /products/{productId} API 
	 * Checks just the status of OK  
	 */
	@Test
	public void testGetProductDetails() throws Exception {
		when(productInteface.getProductDetails(Long.valueOf(13860428)))
				.thenReturn(prodDetails);
		
		mockMvc.perform(get("/products/13860428")
			.contentType("application/json"))
				.andDo(print())
				.andExpect(status().isOk());
		
	}
	
	/*
	 * Triggers a get request to /products/{productId} API 
	 * Validates the product details response
	 */
	@Test
	public void testProductDetailsResponse() throws Exception {
		when(productInteface.getProductDetails(Long.valueOf(13860428)))
				.thenReturn(prodDetails);
		
		mockMvc.perform(get("/products/13860428")
			.contentType("application/json").accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", Matchers.is(13860428)))
				.andExpect(jsonPath("$.name",Matchers.is("The Big Lebowski (Blu-ray)")))
				.andExpect(jsonPath("$.current_price.price",Matchers.is(22.22)));
	}
	
	/*
	 * Triggers a get request to /products/{productId} API 
	 * Validates the exception being thrown for redsky API call
	 */	
	@Test
	public void testProductDetailsResponseForException() throws Exception {
		when(productInteface.getProductDetails(Long.valueOf(13860428)))
				.thenThrow(new ProductNotFound(Long.valueOf(13860428), "Calling Endpoint Error"));
		
		mockMvc.perform(get("/products/13860428")
			.contentType("application/json").accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());

	}

	/*
	 * Triggers a put request to /products/{productId} API 
	 * Checks the status of response for OK status for successfully updated price information
	 */
	@Test
	public void testUpdateProductPrice() throws Exception {
		doNothing().when(productInteface).updateProductPrice(priceForSave);
		
		mockMvc.perform(put("/products/13860428")
				.contentType("application/json").content(objectMapper.writeValueAsString(priceForSave)))
					.andDo(print())
					.andExpect(status().isOk());

	}
	
	/*
	 * Triggers a put request to /products/{productId} API 
	 * Checks for bad request status for missing currency code update requests
	 */	
	@Test
	public void testUpdateProductPriceForMissingCurrencyCode() throws Exception {
		doNothing().when(productInteface).updateProductPrice(priceWithMissingCurrencyCode);
		
		log.info("priceWithMissingCurrencyCode : "+objectMapper.writeValueAsString(priceWithMissingCurrencyCode));
		mockMvc.perform(put("/products/13860428")
				.contentType("application/json").content(objectMapper.writeValueAsString(priceWithMissingCurrencyCode)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.message", Matchers.is("Product price currency Code is not supplied")));

	}	
	
	/*
	 * Triggers a put request to /products/{productId} API 
	 * Checks for bad request status for missing price update requests
	 */		
	
	@Test
	public void testUpdateProductPriceForMissingPrice() throws Exception {
		doNothing().when(productInteface).updateProductPrice(priceWithMissingPrice);
		System.out.println("PriceMissing : "+objectMapper.writeValueAsString(priceWithMissingPrice));
		mockMvc.perform(put("/products/13860428")
				.contentType("application/json").content(objectMapper.writeValueAsString(priceWithMissingPrice)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.message", Matchers.is("Product price is not supplied")));

	}	
	
	/*
	 * Checks the products API endpoint availability
	 */
	@Test
	public void testProductAPIHealth() throws Exception{
		mockMvc.perform(get("/products/apistatus")
				.contentType("application/json")
				.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
	}
	

}
