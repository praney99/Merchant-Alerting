package com.homedepot.mm.pc.merchantalerting.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CORSPathConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if("${respMatrixClientUrl}".equalsIgnoreCase("localhost"))
                    registry.addMapping("/**").allowedOrigins("http://localhost:3000");
                else
                    registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("https://*.homedepot.com");

            }
        };
    }
}
