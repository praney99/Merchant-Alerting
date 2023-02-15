package com.homedepot.mm.pc.merchantalerting.client;

import com.homedepot.mm.pc.merchantalerting.configuration.ClientConfig;
import com.homedepot.mm.pc.merchantalerting.model.RespMatrixResponse;
import com.homedepot.mm.pc.merchantalerting.model.RespMatrixUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RespMatrixClient {
    private final RestTemplate restTemplate;
    private final ClientConfig clientConfig;

    public List<String> getUsersByDcs(String department, String hdClass, String subClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = clientConfig.getRespMatrixClientUrl()
                + "/findUser/DCS"
                + "?d=" + department
                + "&c=" + hdClass
                + "&sc=" + subClass
                + "&json=true";

        ResponseEntity<RespMatrixResponse> respMatrixResp = restTemplate.exchange(url, HttpMethod.GET, entity, RespMatrixResponse.class);
        RespMatrixResponse response = respMatrixResp.getBody();

        if (respMatrixResp.getStatusCode() == HttpStatus.OK && response != null) {
            if (response.getUsers() != null) {
                return response.getUsers().stream()
                        .map(RespMatrixUser::getUserId)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to pull users from responsibility matrix.");
        }
    }
}

