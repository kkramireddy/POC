package com.target.pdp.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.target.pdp.product.domain.ProductDetails;
import com.target.pdp.product.domain.ProductInformation;
import com.target.pdp.product.domain.ProductPrice;
import com.target.pdp.product.repository.ProductPriceRepository;

/**
 * This is a test class for testing products Service layer by mocking all repository calls/EP calls
 * The JSON response from the repository/API calls is supplied to mocking calls loading from JSON files 
 * located in the classpath com/target/pdp/test
 * 
 * @author KK
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTests {
	
	static ObjectMapper objectMapper;
	
	static ProductInformation productInformation;
	static ProductInformation productInfoWithMissingTitle;
	static ProductPrice price;
	static ProductPrice priceForUpdate;
	static String prodinfojsonfilepath = "classpath:com/target/pdp/test/productinformation.json";
	static String prodinfojsonformissingfpath = "classpath:com/target/pdp/test/productinfomissingtitle.json";
	static String pricejsonfilepath = "classpath:com/target/pdp/test/productprice.json";
	static String pricejsonForUpdateFPath = "classpath:com/target/pdp/test/productpriceforupdate.json";
	
	@MockBean
	RestTemplate restTemplate;
	
	@MockBean
	private ProductPriceRepository repository;
	
	@Autowired
	ProductServiceInterface productService;
	


	@Value("${ep.productinfo}")
	String productEP;
	
	@BeforeClass
	public static void setUp() throws Exception {
        File prodInfo = ResourceUtils.getFile(prodinfojsonfilepath);
        File prodPrice = ResourceUtils.getFile(pricejsonfilepath);
        File prodInfoMissingTitle = ResourceUtils.getFile(prodinfojsonformissingfpath);
        File prodPriceForUpdate = ResourceUtils.getFile(pricejsonForUpdateFPath);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        productInformation = objectMapper.readValue(prodInfo, ProductInformation.class);
        price = objectMapper.readValue(prodPrice, ProductPrice.class);
        productInfoWithMissingTitle = objectMapper.readValue(prodInfoMissingTitle, ProductInformation.class);
        priceForUpdate = objectMapper.readValue(prodPriceForUpdate, ProductPrice.class);
        priceForUpdate.setId(Long.valueOf(13860428));
	}
	
	/*
	 * Verifies the product details response returned
	 */
	@Test
	public void testGetProuductDetailsService() {
		
		Optional<ProductPrice> priceOptional = Optional.of(price);
		when(restTemplate.getForObject(productEP, ProductInformation.class, Long.valueOf(13860428))).thenReturn(productInformation);
		when(repository.findById(Long.valueOf(13860428))).thenReturn(priceOptional);
		
		ProductDetails productDetails = productService.getProductDetails(Long.valueOf(13860428));
		assertThat(productDetails.getId()).isEqualTo(13860428);
		assertThat(productDetails.getName()).isEqualTo("The Big Lebowski (Blu-ray)");
		assertThat(productDetails.getProductPrice().getPrice()).isEqualTo(15.22);
		assertThat(productDetails.getProductPrice().getCurrencyCode()).isEqualTo("USD");
		
	}
	
	/*
	 * Verifies if the data store method call is re-attempted for exception to test retry logic
	 */
	@Test
	public void testGetProuductDetailsServiceForDataStoreErrors() {
		

		when(restTemplate.getForObject(productEP, ProductInformation.class, Long.valueOf(13860428))).thenReturn(productInformation);
		when(repository.findById(Long.valueOf(13860428))).thenThrow(new RuntimeException());
		
		assertThatThrownBy(() -> productService.getProductDetails(Long.valueOf(13860428))).isInstanceOf(RuntimeException.class);
		verify(repository,atLeast(2)).findById(Long.valueOf(13860428));
		
	}	
	
	/*
	 * Verifies the product details response returned for missing product description
	 */	
	@Test
	public void testGetProuductDetailsServiceForMissingTitle() {
		Optional<ProductPrice> priceOptional = Optional.of(price);
		when(restTemplate.getForObject(productEP, ProductInformation.class, Long.valueOf(13860428))).thenReturn(productInfoWithMissingTitle);
		when(repository.findById(Long.valueOf(13860428))).thenReturn(priceOptional);
		
		ProductDetails productDetails = productService.getProductDetails(Long.valueOf(13860428));
		assertThat(productDetails.getId()).isEqualTo(13860428);
		assertThat(productDetails.getName()).isEqualTo("UNKNOWN");
		assertThat(productDetails.getProductPrice().getPrice()).isEqualTo(15.22);
		assertThat(productDetails.getProductPrice().getCurrencyCode()).isEqualTo("USD");
	}
	
	/*
	 * Verifies the product details response returned for missing product description
	 */	
	@Test
	public void testupdateProductPrice() {
		Optional<ProductPrice> priceOptional = Optional.of(price);
		when(repository.findById(Long.valueOf(13860428))).thenReturn(priceOptional);
		when(repository.save(priceForUpdate)).thenReturn(priceForUpdate);
		
		
		productService.updateProductPrice(priceForUpdate);
		verify(repository,atLeast(1)).findById(Long.valueOf(13860428));
		verify(repository,atLeast(1)).save(priceForUpdate);

	}	
	
	

}
