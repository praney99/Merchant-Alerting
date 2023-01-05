package com.homedepot.mm.pc.merchantalerting.configuration;

import com.homedepot.mm.pc.merchantalerting.model.DCS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "respmatrix.client")
public class ClientConfig {

    @Value("${respmatrix.client.url}")
    private String dcsEndpoint;
    private String url;

    String queryString;

    private static DCS dcs = null;

    public static final String
        URI_ZONE_DEFINITION_SUBDEPT_SUBCLASS = "/findUser/DCS"
            + "?d=" + dcs.getDepartment()
            + "&c=" + dcs.getClassNumber()
            + "&sc=" + dcs.getSubClassNumber();;


    public static final String CATEGORY_CODE = "1";

}
