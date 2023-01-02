package com.homedepot.mm.pc.merchantalerting.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorDTO {
    private String code;
    private String message;

}
