package com.homedepot.mm.pc.merchantalerting.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Getter
@Configuration
public class ClientConfig {

    @Value("${respMatrixClientUrl}")
    private String respMatrixClientUrl;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
