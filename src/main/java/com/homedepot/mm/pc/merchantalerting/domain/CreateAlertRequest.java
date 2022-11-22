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

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRequest {

        private String systemSource;
        private String type;
        private String templateName;
        private String templateBody;
        private String expirationDate;
        private String keyIdentifiers;

        public static void main(final String[] args) throws JSONException {
                JSONObject key = new JSONObject();
                key.put("sku", "123456");
                key.put("cpi", "0.98");
                System.out.println(key.toString());
                }
        }