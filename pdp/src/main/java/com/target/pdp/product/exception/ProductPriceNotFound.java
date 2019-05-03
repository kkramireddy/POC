package com.target.pdp.product.exception;

public class ProductPriceNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	final Long id;


	public ProductPriceNotFound(Long id,String msg) {
		super(msg);
		this.id = id;
	}

}
