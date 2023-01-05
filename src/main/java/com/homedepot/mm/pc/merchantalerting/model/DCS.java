package com.homedepot.mm.pc.merchantalerting.model;

import com.homedepot.mm.pc.merchantalerting.configuration.ClientConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class DCS {
    String department;
    String classNumber;
    String subClassNumber;

}
