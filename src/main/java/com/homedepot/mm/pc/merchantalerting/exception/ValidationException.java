package com.homedepot.mm.pc.merchantalerting.exception;


import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
private String code;
private HttpStatus status;

    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
