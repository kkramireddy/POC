package com.target.pdp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.target.pdp.product.repository.ProductPriceRepository;

@EnableMongoRepositories(basePackageClasses=ProductPriceRepository.class)
@Configuration
public class PdpMongoConfig {

}
