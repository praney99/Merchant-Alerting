package com.homedepot.mm.pc.merchantalerting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homedepot.mm.pc.merchantalerting.PostgresContainerBaseTest;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import com.homedepot.mm.pc.merchantalerting.template.DefaultTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
public class AlertControllerTest extends PostgresContainerBaseTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private AlertService alertService;

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
}

