package com.target.pdp.product.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails {

	@JsonInclude(Include.NON_NULL)
	Long id;
	
	@JsonInclude(Include.NON_NULL)
	String name;
	
	@JsonProperty("current_price")
	@JsonInclude(Include.NON_NULL)
	ProductPrice productPrice;

}
