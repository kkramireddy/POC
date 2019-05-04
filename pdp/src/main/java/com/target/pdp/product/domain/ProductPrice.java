package com.target.pdp.product.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@Document(collection="productprice")
public class ProductPrice {

	@Id
	@JsonIgnore
	Long id;
	
	@Field(value="currency_code")
	@JsonInclude(Include.NON_NULL)
	@NotBlank(message="{currency.NotBlank}")
	String currencyCode;
	
	@JsonInclude(Include.NON_NULL)
	@NotNull(message="{price.NotBlank}")
	Double value;
	
}
