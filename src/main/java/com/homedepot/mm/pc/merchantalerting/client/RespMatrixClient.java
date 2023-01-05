package com.homedepot.mm.pc.merchantalerting.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.configuration.ClientConfig;
import lombok.extern.slf4j.Slf4j;
//import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RespMatrixClient {

    WebClient webClient;

    ClientConfig webClientConfigurationProperties;

    @Autowired
    public RespMatrixClient(ClientConfig webClientConfigurationProperties) {
        this.webClientConfigurationProperties = webClientConfigurationProperties;
        this.webClient = WebClient.builder()
                .baseUrl(webClientConfigurationProperties.getDcsEndpoint())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    public String getUsersByDcs(String department, String clazz, String subClass) {

        String test_URI_DCS = "/findUser/DCS"
                + "?d=" + department
                + "&c=" + clazz
                + "&sc=" + subClass
                + "&json=true";

        return webClient.get()
                .uri(test_URI_DCS)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError((throwable) -> {
                    log.error("Failed to get zone definition for " +
                            "department:" + department + " class:" + clazz + " subclass:" + subClass);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Exception occurred requesting Users from RespMatrix: " + throwable);
                })
                .block();



    }


    //Filters, stores & returns IDS/LDAPs
    public List<String> getUserIDs(String department, String clazz, String subClass) throws Exception {


        String gold = getUsersByDcs(department, clazz, subClass);
        System.out.println(gold);

        List<String> peach = new ArrayList<>();
//        JsonParser jsonParser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) jsonParser.parse(gold);
//        JSONObject response = new JSONObject(gold);
//        JSONArray array = response.getJSONArray("users");


//        String ldap = response.getJSONObject("users").getString("userId");
//        JSONObject id = (JSONObject) response.get("users");
//        String ldap = response.getJSONObject("users").getString("userId");

//        peach.add(ldap);
        peach.add("apple");
        return peach;
    }
}
