package com.homedepot.mm.pc.merchantalerting.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
