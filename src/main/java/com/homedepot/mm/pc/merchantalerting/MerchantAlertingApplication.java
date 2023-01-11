package com.homedepot.mm.pc.merchantalerting;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@OpenAPIDefinition(info = @Info(title = "Merchant Alerting", description = "Alert creation/management system"))
@SecurityScheme(name = "PingFed", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@ComponentScan(basePackages = {"com.homedepot.mm.pc.merchantalerting", "com.homedepot.appsecurecommunity.resourceserver"})
@SpringBootApplication
public class MerchantAlertingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchantAlertingApplication.class, args);
	}

}
