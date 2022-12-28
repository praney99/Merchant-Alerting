package com.homedepot.mm.pc.merchantalerting.exception;

public class ValidationException extends RuntimeException {

    public ValidationException() {
        super();
    }
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
