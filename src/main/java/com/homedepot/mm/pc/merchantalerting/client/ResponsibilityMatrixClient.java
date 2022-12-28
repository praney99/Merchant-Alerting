package com.homedepot.mm.pc.merchantalerting.client;

import com.homedepot.mm.pc.merchantalerting.exception.NotFoundException;
import com.homedepot.mm.pc.merchantalerting.domain.ResponsibilityMatrixAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponsibilityMatrixClient {

    WebClient webClient;

    final static String URI_BASE = "https://webapps-qa.homedepot.com/RespMatrix/rs/findUser/DCS?";


    @Autowired
    public ResponsibilityMatrixClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<String> fetchLDAP(String request) {

        return webClient.get()
                .uri(URI_BASE + request)
                .retrieve()
                .toEntityList(ResponsibilityMatrixAPIResponse.class)
                .doOnError((throwable) -> {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "exception occurred requesting user details from responsibility matrix: " + throwable);
                })
                .blockOptional()
                .map(responseEntity -> responseEntity.getBody())
                .filter(userList -> !userList.isEmpty())
                .map(userList -> userList.stream().map(userObject -> userObject.getLdap()))
                .orElseThrow(NotFoundException::new)
                .collect(Collectors.toList());
    }
}
