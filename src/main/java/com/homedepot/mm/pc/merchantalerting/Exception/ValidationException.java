package com.homedepot.mm.pc.merchantalerting.Exception;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ValidationException extends RuntimeException {
private String code;
private HttpStatus status;

    public ValidationException(String code,HttpStatus status, String message)
    {
        super(message);
        this.code=code;
        this.status=status;
    }
}
