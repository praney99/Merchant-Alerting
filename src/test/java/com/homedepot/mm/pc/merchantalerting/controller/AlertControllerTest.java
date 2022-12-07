package com.homedepot.mm.pc.merchantalerting.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homedepot.mm.pc.merchantalerting.controller.AlertController;

import com.homedepot.mm.pc.merchantalerting.domain.AlertResponse;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.domain.RetrieveAlertResponse;
import com.homedepot.mm.pc.merchantalerting.processor.AlertService;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static org.apache.coyote.http11.Constants.a;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
public class AlertControllerTest {
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private AlertService alertService;

    private AlertController alertController;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    private MockMvc mvc;

    @Test
    void generateAlertByLdap() throws Exception {

        final String responseString = "c0533e1f-f452-4747-8293-a43cf168ad3f";
        CreateAlertRequest input = new CreateAlertRequest();
        input.setSystemSource("My Assortment");
        input.setType("Regional Assortment");

        JSONObject key = new JSONObject();
        key.put("sku", "123456");
        key.put("cpi", "0.98");

        input.setKeyIdentifiers(key.toString());
        input.setTemplateName("default");

        JSONObject template = new JSONObject();
        template.put("title", "test1");
        template.put("titleDescription", "test2");
        template.put("primaryText1", "test3");
        template.put("primaryLink", "test4");

        input.setTemplateBody(template.toString());
        input.setExpirationDate("2023-09-30");

        ResponseEntity<CreateAlertRequest> mockResponse = new ResponseEntity(input, HttpStatus.OK);

        when(alertService.createAlertByUser(Mockito.any(CreateAlertRequest.class))).thenReturn(String.valueOf(mockResponse));

        when(alertService.createAlertByUser(any())).thenReturn(responseString);

        Assertions.assertNotNull(responseString);
        Assertions.assertTrue(responseString.length() > 0);

        Gson gson = new GsonBuilder().create();
        this.mvc.perform(
                        post("/alert/create")
                                .content(gson.toJson(input)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }


    @Test
    void retrieveAlertByLdap() throws Exception {

        final String requestId = "PXP88N3";
        Date df = new Date();

        AlertResponse response;

        RetrieveAlertResponse output = new RetrieveAlertResponse();

        output.setId(UUID.fromString("c0533e1f-f452-4747-8293-a43cf168ad3f"));


        JSONObject key = new JSONObject();
        key.put("sku", "123456");
        key.put("cpi", "0.98");
        output.setKeyIdentifiers(key.toString());

        output.setSystemSource("My Assortment");

        output.setType("Regional Assortment");

        output.setTemplateName("default");

        JSONObject template = new JSONObject();
        template.put("title", "test1");
        template.put("titleDescription", "test2");
        template.put("primaryText1", "test3");
        template.put("primaryLink", "test4");
        output.setTemplateBody(template.toString());

        output.setCreatedBy("");

        output.setCreateDate(df);

        output.setLastUpdatedBy("");

        output.setLastUpdateDate(df);

        output.setExpirationDate(df);

        response = AlertResponse.builder().alerts(List.of(output)).build();

        ResponseEntity<String> mockResponse = new ResponseEntity(requestId, HttpStatus.OK);

        when(alertService.createAlertByUser(any(CreateAlertRequest.class))).thenReturn(String.valueOf(mockResponse));

        when(alertService.createAlertByUser(any())).thenReturn(String.valueOf(response));

        System.out.println(response);
        Gson gson = new GsonBuilder().create();
        this.mvc.perform(
                        get("/alert/retrieve/requestId").content(gson.toJson(requestId)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.toString().length() > 0);
    }

    @DisplayName("DeleteByAlertId")
    @Test
    void DeleteAlertId() throws Exception {

        final String requestId = "UMQ8QTG";
        final String responseString = "c0533e1f-f452-4747-8293-a43cf168ad3f";
        CreateAlertRequest input = new CreateAlertRequest();
        input.setSystemSource("My Assortment");
        input.setType("Regional Assortment");
        JSONObject key = new JSONObject();
        key.put("sku", "123456");
        key.put("cpi", "0.98");
        input.setKeyIdentifiers(key.toString());
        input.setTemplateName("default");
        JSONObject template = new JSONObject();
        template.put("title", "test1");
        template.put("titleDescription", "test2");
        template.put("primaryText1", "test3");
        template.put("primaryLink", "test4");
        input.setTemplateBody(template.toString());
        input.setExpirationDate("2023-09-30");


        ResponseEntity<CreateAlertRequest> mockResponse = new ResponseEntity(input, HttpStatus.OK);
        when(alertService.createAlertByUser(Mockito.any(CreateAlertRequest.class))).thenReturn(String.valueOf(mockResponse));
        doNothing().doThrow(new RuntimeException()).when(alertService).deleteAlertByAlertId("c0533e1f-f452-4747-8293-a43cf168ad3f");

        when(alertService.createAlertByUser(any())).thenReturn(responseString);


        Gson gson = new GsonBuilder().create();
        this.mvc.perform(
                        delete("/alert/delete/alertId").content(gson.toJson(responseString)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    public String Randomtext(String A) {
        char[] c = A.toCharArray();
        String Random = "";
        for (int x = 0; x < c.length; x++) {
            c[x] = (char) (c[x] + (Math.random() * 10 + 1));
            Random = Random + c[x];
        }
        return Random;
    }

    public Map<String,CreateAlertRequest> CreateAlertRandomRequest(int N) throws Exception {
        int Times = 0;
        Map<String, CreateAlertRequest> MultiMockResponse = new HashMap<>();
        do {
            final String requestId = Randomtext("UMQ8QTG");
            CreateAlertRequest input = new CreateAlertRequest();
            input.setSystemSource("My Assortment");
            input.setType("Regional Assortment");
            JSONObject key = new JSONObject();
            key.put("sku", "123456");
            key.put("cpi", "0.98");
            input.setKeyIdentifiers(key.toString());
            input.setTemplateName("default");
            JSONObject template = new JSONObject();
            template.put("title", "test1");
            template.put("titleDescription", "test2");
            template.put("primaryText1", "test3");
            template.put("primaryLink", "test4");
            input.setTemplateBody(template.toString());
            input.setExpirationDate("2023-09-30");
            MultiMockResponse.put(requestId, input);
    Times++;
        } while (Times < N); return MultiMockResponse;
    }


    @DisplayName("DeleteByAlertRelatedUsersbyAlertId")
    @Test
    void DeleteUsersById() throws Exception {
        final String responseString = "c0533e1f-f452-4747-8293-a43cf168ad3f";
        Map<String,CreateAlertRequest>TestAlert=new HashMap<>();
        TestAlert=CreateAlertRandomRequest(5);
        when(alertService.createAlertByUser(Mockito.any(CreateAlertRequest.class))).thenReturn(String.valueOf(TestAlert));
        doNothing().doThrow(new RuntimeException()).when(alertService).deleteAlertByAlertId("c0533e1f-f452-4747-8293-a43cf168ad3f");
        when(alertService.createAlertByUser(any())).thenReturn(TestAlert.toString());

        Gson gson = new GsonBuilder().create();
        this.mvc.perform(
                        delete("/alert/delete/alertId").content(gson.toJson(responseString)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

}

