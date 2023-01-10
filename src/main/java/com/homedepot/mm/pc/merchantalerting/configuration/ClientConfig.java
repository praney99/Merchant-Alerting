package com.homedepot.mm.pc.merchantalerting.configuration;

import com.homedepot.mm.pc.merchantalerting.model.DCS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties
public class ClientConfig {
    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }


    private String respMatrixClientUrl;

    public String getRespMatrixClientUrl() {
        return respMatrixClientUrl;
    }

    public void setRespMatrixClientUrl(String respMatrixClientUrl) {
        this.respMatrixClientUrl = respMatrixClientUrl;
    }
}
