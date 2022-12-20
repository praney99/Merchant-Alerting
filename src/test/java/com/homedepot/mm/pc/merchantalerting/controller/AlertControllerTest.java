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

        Map<String, String> keyIdentifiers = new HashMap<>();
        keyIdentifiers.put("sku", "123456");
        keyIdentifiers.put("cpi", "0.98");
        alert.setKeyIdentifiers(keyIdentifiers.toString());

        alert.setSystemSource("My Assortment");
        alert.setAlertType("Regional Assortment");
        alert.setTemplateName("default");

        DefaultTemplate defaultTemplate = new DefaultTemplate();
        defaultTemplate.setTitle("title");
        defaultTemplate.setTitleDescription("title description");
        defaultTemplate.setPrimaryText1("primary text 1");
        defaultTemplate.setPrimaryText2("primary text 2");
        defaultTemplate.setTertiaryText("tertiary text");
        defaultTemplate.setPrimaryLinkText("link");
        defaultTemplate.setPrimaryLinkUri("http://localhost:8080");
        alert.setTemplateBody(defaultTemplate.toString());
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
    public Alert generateAlert(String userId, UUID alertId) throws JSONException {

        Alert alert= new Alert();
        alert.setId(alertId);
        alert.setKeyIdentifiers(new JSONObject()
                .put("sku", "123456").put("cpi", "0.98"));
        alert.setSystemSource("My Assortment");
        alert.setType("Regional Assortment");
        alert.setTemplateName("default");
        alert.setTemplateBody(new JSONObject().put("title", "test1")
                .put("titleDescription", "test2")
                .put("primaryText1", "test3")
                .put("primaryLink", "test4"));
        alert.setCreatedBy(userId);
        alert.setCreateDate(new Date());
        alert.setLastUpdatedBy(userId);
        alert.setLastUpdateDate(new Date());
        alert.setExpirationDate(new Date());

        return alert;
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

