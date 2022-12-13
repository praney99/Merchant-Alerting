package com.homedepot.mm.pc.merchantalerting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.expression.ParseException;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRequest {

        private String systemSource;
        private String type;
        private String templateName;
        private Map<String,String> templateBody;
        private String expirationDate;
        private Map<String,String> keyIdentifiers;


}

