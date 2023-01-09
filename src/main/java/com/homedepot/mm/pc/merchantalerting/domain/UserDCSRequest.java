package com.homedepot.mm.pc.merchantalerting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDCSRequest {

    String department;
    String subDepartment;
    @NotEmpty(message = "ClassNumber must not be empty or null ")
    String classNumber;
    @NotEmpty(message = "SubClassNumber must not be empty or null")
    String subClassNumber;

}
