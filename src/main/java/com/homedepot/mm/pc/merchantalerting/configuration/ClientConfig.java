package com.homedepot.mm.pc.merchantalerting.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
public class ClientConfig {
    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }


    @Value("${respMatrixClientUrl}")
    private String respMatrixClientUrl;

}
