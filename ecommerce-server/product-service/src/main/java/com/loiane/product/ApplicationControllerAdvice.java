package com.loiane.product;

import com.loiane.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice that handles exceptions thrown by the controllers.
 */
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(ProductNotFoundException e) {
        return e.getMessage();
    }
}
