package com.homedepot.mm.pc.merchantalerting.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer{
    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins(allowedOrigins)
                            .allowedMethods("*")
                            .allowedHeaders("*");
    }
}
