package com.homedepot.mm.pc.merchantalerting.domain;


import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ToString
public class DCS {
    String department;
    String subDepartment;
    @NotNull
    String classNumber;
    @NotNull
    String subClassNumber;

    public String getDCS()
    {
    return getDepartment()+getSubDepartment()+getClassNumber()+getSubClassNumber();
    }
}
