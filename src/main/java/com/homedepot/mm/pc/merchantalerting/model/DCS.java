package com.homedepot.mm.pc.merchantalerting.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DCS {
    String department;
    String classNumber;
    String subClassNumber;

}
