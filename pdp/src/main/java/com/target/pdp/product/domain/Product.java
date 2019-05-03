package com.target.pdp.product.domain;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class  Product {

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("item")
	Optional<Item> item;

}
