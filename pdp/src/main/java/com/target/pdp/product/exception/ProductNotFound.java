package com.target.pdp.product.exception;

public class ProductNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	final Long id;

	public ProductNotFound(Long id,String msg) {
		super(msg);
		this.id = id;
	}
}
