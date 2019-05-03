package com.target.pdp.product.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.target.pdp.product.domain.Item;
import com.target.pdp.product.domain.Product;
import com.target.pdp.product.domain.ProductDescription;
import com.target.pdp.product.domain.ProductDetails;
import com.target.pdp.product.domain.ProductInformation;
import com.target.pdp.product.domain.ProductPrice;
import com.target.pdp.product.exception.ProductNotFound;
import com.target.pdp.product.exception.ProductPriceNotFound;
import com.target.pdp.product.repository.ProductPriceRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation class for Products API Service Layer.
 * It aggregates product and price details by making calls to redsky API and product price data store.
 * For the information not found, throws exceptions ProductNotFound and ProdutPriceNotFound 
 * 
 * @author KK
 *
 */

@Service
@Slf4j
public class ProductServiceImpl implements ProductServiceInterface{

	
	@Value("${ep.productinfo}")
	public String productEP;
	
	@Autowired
	RetryTemplate retryTemplate;
	
	public ProductServiceImpl(RestTemplate restTemplate,ProductPriceRepository repository) {
		this.restTemplate = restTemplate;
		this.repository = repository;
	}
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private ProductPriceRepository repository;

	/*
	 * (non-Javadoc)
	 * @see com.target.pdp.product.service.ProductServiceInterface#getProductDetails(java.lang.Long)
	 */
	
	public ProductDetails getProductDetails(Long id) {
		log.info("Connecting to EP : " + productEP);
		ProductInformation prodInformation = getProductInfo(id);
		String title = Optional.ofNullable(prodInformation)
							.flatMap(ProductInformation::getProduct)
							.flatMap(Product::getItem)
							.flatMap(Item::getProductDescription)
							.map(ProductDescription::getTitle).orElse("UNKNOWN");
		log.info("Product Name : " + title + " for product Id : " + id);
		ProductDetails prodDetails = new ProductDetails();
		prodDetails.setId(id);
		prodDetails.setName(title);
		
		ProductPrice productPrice = getProductPrice(id);
		prodDetails.setProductPrice(productPrice);
		log.info("Returning results : " + prodDetails);
		return prodDetails;
	}

	/*
	 * (non-Javadoc)
	 * @see com.target.pdp.product.service.ProductServiceInterface#updateProductPrice(com.target.pdp.product.domain.ProductPrice)
	 */

	public void updateProductPrice(ProductPrice productPrice) {
		log.info("Persisting product price for "+productPrice.getId());
		if(repository.findById(productPrice.getId()).isPresent()) {
			repository.save(productPrice);
		}else {
			throw new ProductPriceNotFound(productPrice.getId(),"Product Price is not Found.");
		}

	}
	
	/*
	 * It makes calls to redsky API to get product description details
	 * This is called from getProductDetails interface method
	 * 
	 * @param product Id
	 * @throws ProudctNotFound
	 */
	private ProductInformation getProductInfo(Long id) {
		try {
			return restTemplate.getForObject(productEP, ProductInformation.class, id);
		} catch (RestClientResponseException restCEx) {
			log.error("Error from proudct EP : " + restCEx.getMessage() + " for prouduct id " + id);
			log.error(
					"Error Response from proudct EP : " + restCEx.getResponseBodyAsString() + " for prouduct id " + id);
			throw new ProductNotFound(id, "Proudct is not found from EP : " + productEP);
		}
	}
	
	/*
	 * Interacts with product Price data store and updates price details for the existing product
	 * This is called from getProductDetails method
	 * ReAttempts one more time for errors from the data store
	 * 
	 * @param proudct Id and product price details
	 * @throws ProductPriceNotFound
	 */

	private ProductPrice getProductPrice(Long id) {
		return retryTemplate.execute( 
				(RetryCallback<ProductPrice, RuntimeException>) retryCtx -> {
					if(retryCtx.getRetryCount()>0) {
						log.error("Reattempting times : "+retryCtx.getRetryCount()+" to fetch product price for proudct id..."+id);
					}
			    	return repository.findById(id).orElse(new ProductPrice());					
				});
	}	

}
