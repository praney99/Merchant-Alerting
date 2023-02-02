package com.homedepot.mm.pc.merchantalerting.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.PostgresContainerBaseTest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class AlertServiceIntegrationTest extends PostgresContainerBaseTest {

    @Autowired
    AlertService alertService;

    @Test
    void testExpireCronJob() {
        Alert expiredAlert = populateNewTestAlert();
        expiredAlert.setExpirationDate(Date.valueOf(LocalDate.now().minusDays(1)));

        Alert activeAlert = populateNewTestAlert();
        activeAlert.setExpirationDate(Date.valueOf(LocalDate.now().plusDays(1)));

        Alert persistedExpiredAlert = alertRepository.save(expiredAlert);
        assertNotNull(persistedExpiredAlert);
        Alert persistedActiveAlert = alertRepository.save(activeAlert);
        assertNotNull(persistedActiveAlert);

        String ldap = "foo42br";
        UserAlert userAlertExpired = new UserAlert(ldap, persistedExpiredAlert.getId());
        userAlertExpired.setAlert(persistedExpiredAlert);
        UserAlert userAlertActive = new UserAlert(ldap, persistedActiveAlert.getId());
        userAlertActive.setAlert(persistedActiveAlert);

        UserAlert persistedUserAlertExpired = userAlertRepository.save(userAlertExpired);
        assertNotNull(persistedUserAlertExpired);
        UserAlert persistedUserAlertActive = userAlertRepository.save(userAlertActive);
        assertNotNull(persistedUserAlertActive);

        alertService.cleanupExpiredAlerts();

        Optional<Alert> retrievedActiveAlert = alertRepository.findById(persistedActiveAlert.getId());
        assertTrue(retrievedActiveAlert.isPresent());
        Optional<Alert> retrievedExpiredAlert = alertRepository.findById(persistedExpiredAlert.getId());
        assertFalse(retrievedExpiredAlert.isPresent());
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
