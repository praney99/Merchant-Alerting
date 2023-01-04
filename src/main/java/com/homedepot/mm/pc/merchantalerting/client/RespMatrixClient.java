package com.homedepot.mm.pc.merchantalerting.client;

import com.homedepot.mm.pc.merchantalerting.configuration.ClientConfig;
import com.homedepot.mm.pc.merchantalerting.model.DCS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RespMatrixClient {

    WebClient webClient;

    ClientConfig webClientConfigurationProperties;

    @Autowired
    public RespMatrixClient(ClientConfig webClientConfigurationProperties) {
        this.webClientConfigurationProperties = webClientConfigurationProperties;
        this.webClient = WebClient.builder()
                .baseUrl(webClientConfigurationProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    public Mono<DCS> getUsersByDcs(String department, String clazz, String subClass) {
        return webClient.get()
                .uri(ClientConfig.URI_ZONE_DEFINITION_SUBDEPT_SUBCLASS, department, clazz, subClass)
                .retrieve()
                .bodyToMono(DCS.class)
                .doOnError((throwable) -> {
                    log.error("Failed to get zone definition for " +
                            "department:" + department + " class:" + clazz + " subclass:" + subClass);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Exception occurred requesting Users from RespMatrix: " + throwable);
                });
    }

}
