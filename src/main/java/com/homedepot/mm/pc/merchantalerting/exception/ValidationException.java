package com.homedepot.mm.pc.merchantalerting.exception;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ValidationException extends RuntimeException {
private String code;
private HttpStatus status;

    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
