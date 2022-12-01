package com.homedepot.mm.pc.merchantalerting.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homedepot.mm.pc.merchantalerting.controller.AlertController;

import com.homedepot.mm.pc.merchantalerting.domain.AlertResponse;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.domain.RetrieveAlertResponse;
import com.homedepot.mm.pc.merchantalerting.processor.AlertService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        response=AlertResponse.builder().alerts(List.of(output)).build();

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
}

