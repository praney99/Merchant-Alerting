package com.homedepot.mm.pc.merchantalerting.Exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    String code;
    String message;
}
