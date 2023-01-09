package com.homedepot.mm.pc.merchantalerting.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.PostgresContainerBaseTest;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlertControllerTest extends PostgresContainerBaseTest {

    @LocalServerPort
    private int port;

    @Autowired
    @Qualifier("noErrorRestTemplate")
    RestTemplate restTemplate;

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
}
