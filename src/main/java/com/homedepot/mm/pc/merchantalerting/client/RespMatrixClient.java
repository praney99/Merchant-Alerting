package com.homedepot.mm.pc.merchantalerting.client;

import com.homedepot.mm.pc.merchantalerting.configuration.ClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class RespMatrixClient {

    WebClient webClient;

    ClientConfig webClientConfigurationProperties;

    static List<String> listOfIDS = new ArrayList<>();


    @Autowired
    public RespMatrixClient(ClientConfig webClientConfigurationProperties) {
        this.webClientConfigurationProperties = webClientConfigurationProperties;
        this.webClient = WebClient.builder()
                .baseUrl(webClientConfigurationProperties.getDcsEndpoint())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    public String getUsersByDcs(String department, String hdClass, String subClass) {

        String URI_DCS = "/findUser/DCS"
                + "?d=" + department
                + "&c=" + hdClass
                + "&sc=" + subClass
                + "&json=true";

        return webClient.get()
                .uri(URI_DCS)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError((throwable) -> {
                    log.error("Failed to get zone definition for " +
                            "department:" + department + " class:" + hdClass + " subclass:" + subClass);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Exception occurred requesting Users from RespMatrix: " + throwable);
                })
                .block();
    }


    public List<String> getUserIDs(String department, String hdClass, String subClass) throws Exception {

        String usersByDcs = getUsersByDcs(department, hdClass, subClass);
        JSONObject response = new JSONObject(usersByDcs);
        parseJson(response, "userId");

        return listOfIDS;
    }

    public static void parseJson(JSONObject json, String key) throws Exception {
        boolean exists = json.has(key);
        Iterator<?> keys;
        String nextKeys;


        if(!exists){
            keys = json.keys();
            while (keys.hasNext()){
                nextKeys = (String)keys.next();

                try{
                    if(json.get(nextKeys) instanceof  JSONObject){
                        if(!exists){
                            parseJson(json.getJSONObject(nextKeys), key);
                        }

                    } else if (json.get(nextKeys) instanceof  JSONArray){

                        JSONArray jsonArray = json.getJSONArray(nextKeys);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson= new JSONObject(jsonArrayString);

                            if(!exists){
                                parseJson(innerJson, key);
                            }

                        }

                    }

                } catch (Exception e){
                    throw new Exception(e);
                }
            }
        } else {
            addLdapsToList(json, key);
        }

    }

    public static void addLdapsToList(JSONObject json, String key) throws Exception {

        listOfIDS.add(json.get(key).toString());

        for(int i = 0; i < listOfIDS.size(); i++) {
            for(int j = i+1; j < listOfIDS.size(); j++) {
                if(listOfIDS.get(i).equals(listOfIDS.get(j))) {
                    listOfIDS.remove(j);
                }
            }
        }
    }
}
