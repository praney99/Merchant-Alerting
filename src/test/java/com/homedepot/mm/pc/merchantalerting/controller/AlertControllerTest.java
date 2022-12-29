package com.homedepot.mm.pc.merchantalerting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homedepot.mm.pc.merchantalerting.domain.AlertResponse;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.processor.AlertService;
import com.homedepot.mm.pc.merchantalerting.template.DefaultTemplate;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.apache.commons.lang3.Range.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
public class AlertControllerTest {
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private AlertService alertService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    private MockMvc mvc;
    private final ObjectMapper mapper= new ObjectMapper();

    @Test
    void generateAlertByLdap() throws Exception {
        final String ldap = "fo42br";
        CreateAlertRequest alertRequest = new CreateAlertRequest();
        alertRequest.setAlertType("Regional Assortment");
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

        alert.setKeyIdentifiers(
                new JSONObject().put("sku", "123456").put("cpi", "0.98").toString());

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
        alert.setTemplateBody(defaultTemplate.toJSONObject(defaultTemplate).toString());
        alert.setCreateBy("unit test");
        alert.setCreateDate(new Date(System.currentTimeMillis()));
        alert.setLastUpdateBy("");
        alert.setLastUpdateDate(null);
        alert.setExpirationDate(null);

        when(alertService.getAlertsByLdap(anyString()))
                .thenReturn(List.of(alert));

        this.mvc.perform(get("/alert/user/" + ldap)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
    @Test
    @DisplayName("SaveAlertsByDCS")
    void generateAlertByDCS() throws Exception {
        final String dcs = "001-001-001";
        CreateAlertRequest alertRequest = new CreateAlertRequest();
        alertRequest.setAlertType("Regional Assortment");
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
        when(alertService.createAlertWithLdapAssociations(any(), List.of(anyString())))
                .thenReturn(alertRequest.toAlert());
        System.out.println(alertRequest);
        AlertController AL= new AlertController(alertService);
        System.out.println(AL.validateDCS(dcs));
        final String SaveBy_DCS_URL="/alert/dcs/";

        //1°Test SaveSuccess
        this.mvc.perform(MockMvcRequestBuilders
                .post( SaveBy_DCS_URL+ dcs)
                .content(mapper.writeValueAsBytes(alertRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //2°Test MissingDCS
        this.mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL)
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(405));

        //3° MissingBody
         this.mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ dcs)
                        .content(mapper.writeValueAsBytes(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

         //4°Test WrongSize DCS
        this.mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ "0")
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

        //5°Test Zero DCS
        this.mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ "000-000-000")
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

        //6°Test WrongLetters DCS
        this.mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ "001-00A-001")
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
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

