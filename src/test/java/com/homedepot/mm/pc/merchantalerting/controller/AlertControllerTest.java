package com.homedepot.mm.pc.merchantalerting.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import com.homedepot.mm.pc.merchantalerting.template.DefaultTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
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

import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
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
        final String ldap = "fo42br";
        CreateAlertRequest alertRequest = new CreateAlertRequest();
        alertRequest.setType("Regional Assortment");
        alertRequest.setSystemSource("My Assortment");
        alertRequest.setExpirationDate(null);
        alertRequest.setKeyIdentifiers(null);

        Map<String, String> keyIdentifiers = new HashMap<>();
        keyIdentifiers.put("sku", "123456");
        keyIdentifiers.put("cpi", "0.98");

        alertRequest.setKeyIdentifiers(keyIdentifiers);
        alertRequest.setTemplateName("default");

        DefaultTemplate defaultTemplate = new DefaultTemplate();
        defaultTemplate.setTitle("title");
        defaultTemplate.setTitleDescription("title description");
        defaultTemplate.setPrimaryText1("primary text 1");
        defaultTemplate.setPrimaryText2("primary text 2");
        defaultTemplate.setTertiaryText("tertiary text");
        defaultTemplate.setPrimaryLinkText("link");
        defaultTemplate.setPrimaryLinkUri("http://localhost:8080");
        alertRequest.setTemplateBody(new ObjectMapper().convertValue(defaultTemplate, HashMap.class));

        when(alertService.createAlertByLdap(any(), anyString()))
                .thenReturn(new Alert());

        Gson gson = new GsonBuilder().create();
        this.mvc.perform(
                        post("/alert/user/" + ldap)
                                .content(gson.toJson(alertRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }


    @Test
    void retrieveAlertByLdap() throws Exception {

        final String ldap = "PXP88N3";
        Alert alert = new Alert();

        JSONObject keyIdentifiers = new JSONObject();
        keyIdentifiers.put("sku", "123456");
        keyIdentifiers.put("cpi", "0.98");
        alert.setKeyIdentifiers(keyIdentifiers);

        alert.setSystemSource("My Assortment");
        alert.setAlertType("Regional Assortment");
        alert.setTemplateName("default");

        JSONObject defaultTemplate = new JSONObject();
        defaultTemplate.put("title", "test1");
        defaultTemplate.put("titleDescription", "test2");
        defaultTemplate.put("primaryText1", "test3");
        defaultTemplate.put("primaryLink", "test4");
        defaultTemplate.put("primaryLinkUri", "http://localhost:8080");
        alert.setTemplateBody(defaultTemplate);
        alert.setCreateBy("unit test");
        alert.setCreateDate(new Date(System.currentTimeMillis()));
        alert.setLastUpdateBy(null);
        alert.setLastUpdateDate(null);
        alert.setExpirationDate(null);

        when(alertService.getAlertsByLdap(anyString()))
                .thenReturn(List.of(alert));

        this.mvc.perform(get("/alert/user/" + ldap)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @DisplayName("DeleteAlertsById")
    @Test
    void deleteAlertsById() throws Exception{
        UUID alertId= UUID.fromString("c0533e1f-f452-4747-8293-a43cf168ad3f");
        Mockito.doNothing().when(alertService).deleteAlertByAlertId(Mockito.any());
        this.mvc.perform(delete("/alert/{alertId}", alertId).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }


}

