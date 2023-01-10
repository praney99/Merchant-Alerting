package com.homedepot.mm.pc.merchantalerting.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.homedepot.mm.pc.merchantalerting.PostgresContainerBaseTest;
import com.homedepot.mm.pc.merchantalerting.client.RespMatrixClient;
import com.homedepot.mm.pc.merchantalerting.domain.AlertTemplateType;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AlertControllerTest extends PostgresContainerBaseTest {

    @LocalServerPort
    private int port;

    AlertService alertService;

    @Autowired
    @Qualifier("noErrorRestTemplate")
    RestTemplate restTemplate;

    @MockBean
    RespMatrixClient respMatrixClient;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
        alertRequest.setTemplateName(AlertTemplateType.DEFAULT);

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
    void generateAlertByDCS() throws Exception {
        final String dcs = "001-001-001";
        CreateAlertRequest alertRequest = new CreateAlertRequest();
        alertRequest.setType("Regional Assortment");
        alertRequest.setSystemSource("My Assortment");
        alertRequest.setExpirationDate(null);
        Map<String, String> keyIdentifiers = new HashMap<>();
        keyIdentifiers.put("sku", "123456");
        keyIdentifiers.put("store", "123");
        alertRequest.setKeyIdentifiers(keyIdentifiers);
        Map<String, String> defaultTemplate = new HashMap<>();
        defaultTemplate.put("title", "title");
        defaultTemplate.put("titleDescription", "title description");
        defaultTemplate.put("primaryText1", "primary text 1");
        defaultTemplate.put("primaryText2", "primary text 2");
        defaultTemplate.put("tertiaryText", "tertiary text");
        defaultTemplate.put("primaryLinkText", "link");
        defaultTemplate.put("primaryLinkUri", "http://localhost:8080");
        alertRequest.setTemplateBody(defaultTemplate);
        alertRequest.setTemplateName(AlertTemplateType.DEFAULT);

        when(respMatrixClient.getUsersByDcs(any(), any(), any())).thenReturn(List.of("bcb44jc"));

        ResponseEntity<Alert> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/" + dcs,
                alertRequest, Alert.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        ResponseEntity<Alert[]> alertsByLdap = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/alert/user/bcb44jc", Alert[].class);

        Alert createdAlert = alertsByLdap.getBody()[0];
        assertTrue(createdAlert.getExpirationDate().after(Date.valueOf(LocalDate.now().plusDays(29))));
        assertEquals("primary text 1", createdAlert.getTemplateBody().get("primaryText1").asText());
        assertEquals(AlertTemplateType.DEFAULT.toString().toLowerCase(), createdAlert.getTemplateName());
        assertEquals("123456", createdAlert.getKeyIdentifiers().get("sku").asText());

    }

    @Test
    void testRequiredAlertFields() {
        final String dcs = "001-001-001";
        CreateAlertRequest alertRequest = new CreateAlertRequest();
        alertRequest.setType("Regional Assortment");
        alertRequest.setSystemSource(null); // Required Field
        alertRequest.setTemplateName(AlertTemplateType.DEFAULT);

        when(respMatrixClient.getUsersByDcs(any(), any(), any())).thenReturn(List.of("bcb44jc"));

        ResponseEntity<Alert> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/" + dcs,
                alertRequest, Alert.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        alertRequest.setType(null); // Required Field
        alertRequest.setSystemSource("test");
        alertRequest.setTemplateName(AlertTemplateType.DEFAULT);

        responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/" + dcs,
                alertRequest, Alert.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testFutureExpirationDate() {
        final String dcs = "001-001-001";
        CreateAlertRequest alertRequest = new CreateAlertRequest();
        alertRequest.setType("Regional Assortment");
        alertRequest.setSystemSource("test source");
        alertRequest.setTemplateName(AlertTemplateType.DEFAULT);
        alertRequest.setExpirationDate(LocalDate.now().minusDays(1));

        when(respMatrixClient.getUsersByDcs(any(), any(), any())).thenReturn(List.of("bcb44jc"));

        ResponseEntity<Alert> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/" + dcs,
                alertRequest, Alert.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testAlertTemplateName() {
        final String dcs = "001-001-001";
        final String alertRequest = "{\n" +
                "  \"systemSource\": \"mpulse\",\n" +
                "  \"type\": \"test\",\n" +
                "  \"templateName\": \"someOtherTemplate\"\n" +
                "}";

        when(respMatrixClient.getUsersByDcs(any(), any(), any())).thenReturn(List.of("bcb44jc"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Alert> responseEntity = restTemplate.exchange("http://localhost:" + port + "/alert/dcs/" + dcs, HttpMethod.POST,
                new HttpEntity<>(alertRequest, headers), Alert.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testDcsValidations() {

    }
}
