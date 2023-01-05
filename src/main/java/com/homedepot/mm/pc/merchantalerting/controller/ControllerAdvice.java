package com.homedepot.mm.pc.merchantalerting.controller;

import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value= ValidationException.class)
    public ResponseEntity<ErrorDTO>ValidationExceptionHandler(ValidationException ex) {
        ErrorDTO error = ErrorDTO.builder().code(ex.getCode()).message(ex.getMessage()).build();
        return new ResponseEntity<>(error, ex.getStatus());
    }

}
