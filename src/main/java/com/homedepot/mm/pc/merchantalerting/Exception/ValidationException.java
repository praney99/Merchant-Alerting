package com.homedepot.mm.pc.merchantalerting.Exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
