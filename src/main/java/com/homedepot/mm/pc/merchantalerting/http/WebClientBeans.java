package com.homedepot.mm.pc.merchantalerting.http;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientBeans {

    @Bean
    WebClient webClient(){
        return WebClient.builder().build();
    }

}
