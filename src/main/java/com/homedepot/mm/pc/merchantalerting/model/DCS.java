package com.homedepot.mm.pc.merchantalerting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DCS {
    @Pattern(regexp = "^\\d{0,3}$")
    String department;
    @Pattern(regexp = "^((\\d{1,3}[a-zA-Z])|(\\d{1,4}))$")
    String subDepartment;
    @NotEmpty @Pattern(regexp = "^\\d{0,3}$")
    String classNumber;
    @NotEmpty @Pattern(regexp = "^\\d{0,3}$")
    String subClassNumber;

    @JsonIgnore
    public String getDCS()
    {
        return String.join("-", List.of(
                        StringUtils.leftPad(this.getDepartment(), 3, "0"),
                        StringUtils.leftPad(this.getSubDepartment().toUpperCase(), 4, "0"),
                        StringUtils.leftPad(this.getClassNumber(), 3, "0"),
                        StringUtils.leftPad(this.getSubClassNumber(), 3, "0")
                )
        );
    }

}
