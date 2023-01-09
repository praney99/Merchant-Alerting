package com.homedepot.mm.pc.merchantalerting.configuration;

import com.homedepot.mm.pc.merchantalerting.model.DCS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "respmatrix-client-url")
public class ClientConfig {

    @Value("${respmatrix-client-url}")
    private String dcsEndpoint;

    @Autowired
    public static DCS dcs = new DCS();

}
