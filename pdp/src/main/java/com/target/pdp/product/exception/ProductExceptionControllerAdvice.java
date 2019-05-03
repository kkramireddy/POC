package com.target.pdp.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.target.pdp.product.domain.ProductDetails;
import com.target.pdp.product.domain.ProductPrice;
import com.target.pdp.product.exception.vo.PriceErrorVO;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ProductExceptionControllerAdvice {

	
	@ExceptionHandler(ProductNotFound.class)
	public ResponseEntity<ProductDetails> productNotFoundExceptionHandler(ProductNotFound pnF){
		log.error(pnF.getMessage()+" for proudct id "+pnF.id);
		return new ResponseEntity<>(new ProductDetails(),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ProductPriceNotFound.class)
	public ResponseEntity<ProductPrice> productPriceNotFoundExceptionHandler(ProductPriceNotFound ppNF){
		log.info(ppNF.getMessage()+" for proudct id "+ppNF.id);
		return new ResponseEntity<>(new ProductPrice(),HttpStatus.NOT_FOUND);
	}
	
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public PriceErrorVO handleUpdateProdcutPriceValidationError(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        return new PriceErrorVO("VALIDATION_FAILED", defaultMessage);
    }
}
