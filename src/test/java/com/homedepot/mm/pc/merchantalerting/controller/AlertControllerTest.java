package com.homedepot.mm.pc.merchantalerting.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.PostgresContainerBaseTest;
import com.homedepot.mm.pc.merchantalerting.client.RespMatrixClient;
import com.homedepot.mm.pc.merchantalerting.domain.AlertTemplateType;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static com.homedepot.mm.pc.merchantalerting.TestUtils.getUserJwtToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AlertControllerTest extends PostgresContainerBaseTest {

    @LocalServerPort
    private int port;

    @Autowired
    @Qualifier("noErrorRestTemplate")
    RestTemplate restTemplate;

    @MockBean
    RespMatrixClient respMatrixClient;

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
    void generateAlertByDCS() {
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
        CreateAlertRequest alertRequest = new CreateAlertRequest();
        alertRequest.setType("Regional Assortment");
        alertRequest.setSystemSource("test source");
        alertRequest.setTemplateName(AlertTemplateType.DEFAULT);

        when(respMatrixClient.getUsersByDcs(any(), any(), any())).thenReturn(List.of("bcb44jc"));

        ResponseEntity<Alert> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/cat",
                alertRequest, Alert.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/12345-123-123",
                alertRequest, Alert.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/123-abc-123",
                alertRequest, Alert.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/003-001-123a",
                alertRequest, Alert.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testNoUsersForRequestedDCS() {
        CreateAlertRequest alertRequest = new CreateAlertRequest();
        alertRequest.setType("Regional Assortment");
        alertRequest.setSystemSource("test source");
        alertRequest.setTemplateName(AlertTemplateType.DEFAULT);

        // No users for the input DCS.
        when(respMatrixClient.getUsersByDcs(any(), any(), any())).thenReturn(List.of());

        ResponseEntity<Alert> responseEntity = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/alert/dcs/001-001-001",
                alertRequest, Alert.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testDismissAlertHappyPath() {
        Alert alert_0 = populateNewTestAlert();
        Alert alert_1 = populateNewTestAlert();

        Alert persistedAlert_0 = alertRepository.save(alert_0);
        assertNotNull(persistedAlert_0);
        Alert persistedAlert_1 = alertRepository.save(alert_1);
        assertNotNull(persistedAlert_1);

        String ldap = "mc62ye";
        UserAlert userAlert_0 = new UserAlert(ldap, persistedAlert_0.getId());
        userAlert_0.setAlert(persistedAlert_0);
        UserAlert userAlert_1 = new UserAlert(ldap, persistedAlert_1.getId());
        userAlert_1.setAlert(persistedAlert_1);

        UserAlert persistedUserAlert_0 = userAlertRepository.save(userAlert_0);
        assertNotNull(persistedUserAlert_0);
        UserAlert persistedUserAlert_1 = userAlertRepository.save(userAlert_1);
        assertNotNull(persistedUserAlert_1);

        Map<UUID, Boolean> alertDismissalStates = new HashMap<>();
        alertDismissalStates.put(persistedAlert_0.getId(), true);
        alertDismissalStates.put(persistedAlert_1.getId(), false);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getUserJwtToken());
        HttpEntity<Map<UUID, Boolean>> request = new HttpEntity<>(alertDismissalStates, headers);

        this.restTemplate.postForObject(
                "http://localhost:" + port + "/alert/user/" + ldap + "/dismiss",
                request, Void.class);

        UserAlertId userAlertId_0 = new UserAlertId(
                persistedUserAlert_0.getLdap(),
                persistedUserAlert_0.getAlertId()
        );
        Optional<UserAlert> optionalUserAlert_0 = userAlertRepository.findById(userAlertId_0);
        assertTrue(optionalUserAlert_0.isPresent());
        UserAlert dismissedUserAlert_0 = optionalUserAlert_0.get();
        assertNotNull(dismissedUserAlert_0);
        assertTrue(dismissedUserAlert_0.getIsDismissed());
        assertNotNull(dismissedUserAlert_0.getLastUpdated());
        assertNotNull(dismissedUserAlert_0.getLastUpdateBy());

        UserAlertId userAlertId_1 = new UserAlertId(
                persistedUserAlert_1.getLdap(),
                persistedUserAlert_1.getAlertId()
        );
        Optional<UserAlert> optionalUserAlert_1 = userAlertRepository.findById(userAlertId_1);
        assertTrue(optionalUserAlert_1.isPresent());
        UserAlert dismissedUserAlert_1 = optionalUserAlert_1.get();
        assertNotNull(dismissedUserAlert_1);
        assertFalse(dismissedUserAlert_1.getIsDismissed());
        assertNotNull(dismissedUserAlert_1.getLastUpdated());
        assertNotNull(dismissedUserAlert_1.getLastUpdateBy());
    }

    @Test
    void testDismissAlertFailure() {
        Alert alert_0 = populateNewTestAlert();
        Alert alert_1 = populateNewTestAlert();

        Alert persistedAlert_0 = alertRepository.save(alert_0);
        assertNotNull(persistedAlert_0);
        Alert persistedAlert_1 = alertRepository.save(alert_1);
        assertNotNull(persistedAlert_1);

        String ldap = "mc62ye";
        UserAlert userAlert_0 = new UserAlert(ldap, persistedAlert_0.getId());
        userAlert_0.setAlert(persistedAlert_0);
        UserAlert userAlert_1 = new UserAlert(ldap, persistedAlert_1.getId());
        userAlert_1.setAlert(persistedAlert_1);

        UserAlert persistedUserAlert_0 = userAlertRepository.save(userAlert_0);
        assertNotNull(persistedUserAlert_0);
        UserAlert persistedUserAlert_1 = userAlertRepository.save(userAlert_1);
        assertNotNull(persistedUserAlert_1);

        Map<UUID, Boolean> alertDismissalStates = new HashMap<>();
        alertDismissalStates.put(persistedAlert_0.getId(), true);

        // Test for failure with an ID that does not exist in the system.
        UUID nonexistentAlertId = UUID.randomUUID();
        alertDismissalStates.put(nonexistentAlertId, true);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getUserJwtToken());
        HttpEntity<Map<UUID, Boolean>> request = new HttpEntity<>(alertDismissalStates, headers);

        this.restTemplate.postForObject(
                "http://localhost:" + port + "/alert/user/" + ldap + "/dismiss",
                request, Void.class);

        UserAlertId userAlertId_0 = new UserAlertId(
                persistedUserAlert_0.getLdap(),
                persistedUserAlert_0.getAlertId()
        );
        Optional<UserAlert> optionalUserAlert_0 = userAlertRepository.findById(userAlertId_0);
        assertTrue(optionalUserAlert_0.isPresent());
        UserAlert dismissedUserAlert_0 = optionalUserAlert_0.get();
        assertNotNull(dismissedUserAlert_0);
        // The SECOND alert id did not exist. This assertion tests that the FIRST alert,
        // whose dismiss was originally set to true, was rolled back after the failure.
        assertFalse(dismissedUserAlert_0.getIsDismissed());
        assertNull(dismissedUserAlert_0.getLastUpdated());
        assertNull(dismissedUserAlert_0.getLastUpdateBy());
    }

    private Alert populateNewTestAlert() {
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

        return alert;
    }
}