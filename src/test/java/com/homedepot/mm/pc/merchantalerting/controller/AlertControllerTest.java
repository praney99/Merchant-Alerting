package com.homedepot.mm.pc.merchantalerting.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.Exception.ValidationDCSException;
import com.homedepot.mm.pc.merchantalerting.PostgresContainerBaseTest;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlertControllerTest extends PostgresContainerBaseTest {

    @LocalServerPort
    private int port;

    @Autowired
    @Qualifier("noErrorRestTemplate")
    RestTemplate restTemplate;

    @Autowired
    private WebApplicationContext context;
    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }
    @MockBean
    AlertService alertService;
    private MockMvc mvc;
    private final ObjectMapper mapper= new ObjectMapper();

    @Test
    void generateAlertByLdap() {
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

        Map<String, String> defaultTemplate = new HashMap<>();
        defaultTemplate.put("title","title");
        defaultTemplate.put("titleDescription","title description");
        defaultTemplate.put("primaryText1","primary text 1");
        defaultTemplate.put("primaryText2","primary text 2");
        defaultTemplate.put("tertiaryText","tertiary text");
        defaultTemplate.put("primaryLinkText","link");
        defaultTemplate.put("primaryLinkUri","http://localhost:8080");
        alertRequest.setTemplateBody(defaultTemplate);

        ResponseEntity<Alert> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/user/" + ldap,
                alertRequest, Alert.class);

        assertNotNull(responseEntity);
    }


    @Test
    void retrieveAlertByLdap() {
        Alert alert = new Alert();
        alert.setSystemSource("My Assortment");
        alert.setAlertType("Regional Assortment");
        alert.setCreateBy("unit test");
        alert.setCreated(new Timestamp(System.currentTimeMillis()));
        alert.setLastUpdateBy(null);
        alert.setLastUpdated(null);
        alert.setExpirationDate(null);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> keyIdentifiers = new HashMap<>();
        keyIdentifiers.put("sku", "123456");
        keyIdentifiers.put("cpi", "0.98");
        alert.setKeyIdentifiers(mapper.convertValue(keyIdentifiers, JsonNode.class));

        Map<String, String> defaultTemplate = new HashMap<>();
        defaultTemplate.put("title","title");
        defaultTemplate.put("titleDescription","title description");
        defaultTemplate.put("primaryText1","primary text 1");
        defaultTemplate.put("primaryText2","primary text 2");
        defaultTemplate.put("tertiaryText","tertiary text");
        defaultTemplate.put("primaryLinkText","link");
        defaultTemplate.put("primaryLinkUri","http://localhost:8080");
        alert.setTemplateBody(mapper.convertValue(defaultTemplate, JsonNode.class));
        alert.setTemplateName("default");

        Alert persistedAlert = alertRepository.save(alert);
        assertNotNull(persistedAlert);

        String ldap = "foo42br";
        UserAlert userAlert = new UserAlert(ldap, persistedAlert.getId());
        userAlert.setAlert(persistedAlert);

        UserAlert persistedUserAlert = userAlertRepository.save(userAlert);
        assertNotNull(persistedUserAlert);

        ResponseEntity<Alert[]> responseEntity = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/alert/user/" + ldap, Alert[].class);

        assertNotNull(responseEntity);
        assertEquals(1, responseEntity.getBody() == null ? 0 : responseEntity.getBody().length);
        assertEquals(responseEntity.getBody()[0].getId(), persistedAlert.getId());
    }

    @Test
    void retrieveAlertById() {
        Alert alert = new Alert();
        alert.setSystemSource("My Assortment");
        alert.setAlertType("Regional Assortment");
        alert.setCreateBy("unit test");
        alert.setCreated(new Timestamp(System.currentTimeMillis()));
        alert.setLastUpdateBy(null);
        alert.setLastUpdated(null);
        alert.setExpirationDate(null);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> keyIdentifiers = new HashMap<>();
        keyIdentifiers.put("sku", "123456");
        keyIdentifiers.put("cpi", "0.98");
        alert.setKeyIdentifiers(mapper.convertValue(keyIdentifiers, JsonNode.class));

        Map<String, String> defaultTemplate = new HashMap<>();
        defaultTemplate.put("title","title");
        defaultTemplate.put("titleDescription","title description");
        defaultTemplate.put("primaryText1","primary text 1");
        defaultTemplate.put("primaryText2","primary text 2");
        defaultTemplate.put("tertiaryText","tertiary text");
        defaultTemplate.put("primaryLinkText","link");
        defaultTemplate.put("primaryLinkUri","http://localhost:8080");
        alert.setTemplateBody(mapper.convertValue(defaultTemplate, JsonNode.class));
        alert.setTemplateName("default");

        Alert persistedAlert = alertRepository.save(alert);
        assertNotNull(persistedAlert);

        ResponseEntity<Alert> responseEntity = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/alert/" + persistedAlert.getId(), Alert.class);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getId(), persistedAlert.getId());
    }

    @Test
    void deleteAlertByIdCascadeUserAlertDelete() {
        Alert alert = new Alert();
        alert.setSystemSource("My Assortment");
        alert.setAlertType("Regional Assortment");
        alert.setCreateBy("unit test");
        alert.setCreated(new Timestamp(System.currentTimeMillis()));
        alert.setLastUpdateBy(null);
        alert.setLastUpdated(null);
        alert.setExpirationDate(null);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> keyIdentifiers = new HashMap<>();
        keyIdentifiers.put("sku", "123456");
        keyIdentifiers.put("cpi", "0.98");
        alert.setKeyIdentifiers(mapper.convertValue(keyIdentifiers, JsonNode.class));

        Map<String, String> defaultTemplate = new HashMap<>();
        defaultTemplate.put("title","title");
        defaultTemplate.put("titleDescription","title description");
        defaultTemplate.put("primaryText1","primary text 1");
        defaultTemplate.put("primaryText2","primary text 2");
        defaultTemplate.put("tertiaryText","tertiary text");
        defaultTemplate.put("primaryLinkText","link");
        defaultTemplate.put("primaryLinkUri","http://localhost:8080");
        alert.setTemplateBody(mapper.convertValue(defaultTemplate, JsonNode.class));
        alert.setTemplateName("default");

        Alert persistedAlert = alertRepository.save(alert);
        assertNotNull(persistedAlert);

        String ldap0 = "foo42br";
        String ldap1 = "fzz24bz";
        UserAlert userAlert0 = new UserAlert(ldap0, persistedAlert.getId());
        userAlert0.setAlert(persistedAlert);
        UserAlert userAlert1 = new UserAlert(ldap1, persistedAlert.getId());
        userAlert1.setAlert(persistedAlert);

        UserAlert persistedUserAlert0 = userAlertRepository.save(userAlert0);
        assertNotNull(persistedUserAlert0);
        UserAlert persistedUserAlert1 = userAlertRepository.save(userAlert1);
        assertNotNull(persistedUserAlert1);

        this.restTemplate.delete("http://localhost:" + port + "/alert/" + persistedAlert.getId());

        List<Alert> deletedAlerts = alertRepository.findAllById(List.of(persistedAlert.getId()));
        assertEquals(0, deletedAlerts.size());

        List<UserAlertId> userAlertIds = List.of(
          new UserAlertId(persistedUserAlert0.getLdap(), persistedUserAlert0.getAlertId()),
          new UserAlertId(persistedUserAlert1.getLdap(), persistedUserAlert1.getAlertId())
        );
        List<UserAlert> deletedUserAlerts = userAlertRepository.findAllById(userAlertIds);
        assertEquals(0, deletedUserAlerts.size());
    }
    @Test
    @DisplayName("SaveAlertsByDCS")
    void generateAlertByDCS() throws Exception {
        final String dcs = "001-001-001";
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
        Map<String, String> defaultTemplate = new HashMap<>();
        defaultTemplate.put("title","title");
        defaultTemplate.put("titleDescription","title description");
        defaultTemplate.put("primaryText1","primary text 1");
        defaultTemplate.put("primaryText2","primary text 2");
        defaultTemplate.put("tertiaryText","tertiary text");
        defaultTemplate.put("primaryLinkText","link");
        defaultTemplate.put("primaryLinkUri","http://localhost:8080");
        alertRequest.setTemplateBody(new ObjectMapper().convertValue(defaultTemplate, HashMap.class));
        alertRequest.setTemplateName("default");

        when(alertService.createAlertWithLdapAssociations(any(), List.of(anyString())))
                .thenReturn(alertRequest.toAlert());
        System.out.println(alertRequest);
        ValidationDCSException AL= new ValidationDCSException();
        System.out.println(AL.validateDCS(dcs));
        final String SaveBy_DCS_URL="/alert/dcs/";

        //1°Test SaveSuccess
        mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ dcs)
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //2°Test MissingDCS
        mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL)
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(405));

        //3° MissingBody
        mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ dcs)
                        .content(mapper.writeValueAsBytes(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

        //4°Test WrongSize DCS
        mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ "0")
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

        //5°Test Zero DCS
        mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ "000-000-000")
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

        //6°Test WrongLetters DCS
        mvc.perform(MockMvcRequestBuilders
                        .post( SaveBy_DCS_URL+ "001-00A-001")
                        .content(mapper.writeValueAsBytes(alertRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

}

