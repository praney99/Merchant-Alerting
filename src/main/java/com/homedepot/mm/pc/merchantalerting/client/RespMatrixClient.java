package com.homedepot.mm.pc.merchantalerting.client;

import com.homedepot.mm.pc.merchantalerting.configuration.ClientConfig;
import com.homedepot.mm.pc.merchantalerting.model.RespMatrixReponse;
import com.homedepot.mm.pc.merchantalerting.model.RespMatrixUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class RespMatrixClient {

    @Autowired
    RestTemplate restTemplate;

    ClientConfig clientConfig;


    @Autowired
    public RespMatrixClient(ClientConfig clientConfig, RestTemplate restTemplate) {
        this.clientConfig = clientConfig;
        this.restTemplate = restTemplate;
    }

    public List<String> getUsersByDcs(String department, String hdClass, String subClass) throws JSONException {
        List<String> users = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String URI_DCS = clientConfig.getRespMatrixClientUrl()
                + "/findUser/DCS"
                + "?d=" + department
                + "&c=" + hdClass
                + "&sc=" + subClass
                + "&json=true";

        ResponseEntity<RespMatrixReponse> respMatrixResp = restTemplate.exchange(URI_DCS, HttpMethod.GET, entity, RespMatrixReponse.class);
        String response = respMatrixResp.getBody().getUsers().get(0).toString();
        JSONArray respArry = new JSONArray(response);

        for (int i = 0; i < respArry.length(); i++) {
            JSONObject respObj = respArry.getJSONObject(i);
            String ldap = respObj.getString("userId");
            users.add(ldap);
        }


        return users;

    }
}

