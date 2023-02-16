package com.homedepot.mm.pc.merchantalerting.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.PostgresContainerBaseTest;
import com.homedepot.mm.pc.merchantalerting.entity.Alert;
import com.homedepot.mm.pc.merchantalerting.entity.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.homedepot.mm.pc.merchantalerting.TestUtils.DEFAULT_KEY_IDENTIFIERS;
import static com.homedepot.mm.pc.merchantalerting.TestUtils.DEFAULT_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class AlertServiceIntTest extends PostgresContainerBaseTest {

    @Autowired
    private AlertService alertService;

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

    @Test
    void updateAlertReadStatus_UserAlertIsExist_UpdateReadStatusAndLastUpdate() {
        Alert expectedAlert = populateNewTestAlert();
        Alert persistedAlert = alertRepository.save(expectedAlert);

        assertNotNull(persistedAlert);

        UUID expectedUserAlertId = persistedAlert.getId();

        Map<UUID, Boolean> expectedReadStatus = Map.of(
                expectedUserAlertId, true
        );

        String expectedLdap = "dxb87mu";
        UserAlert expectedUserAlert = new UserAlert(expectedLdap, expectedUserAlertId);
        expectedUserAlert.setAlert(persistedAlert);
        expectedUserAlert.setReadStatus(false);

        UserAlert persistedUserAlert = userAlertRepository.save(expectedUserAlert);

        assertNotNull(persistedUserAlert);

        String expectedUpdateBy = "mc62ye";
        alertService.updateAlertReadStatus(expectedLdap, expectedUpdateBy, expectedReadStatus);

        Optional<UserAlert> retrievedUserAlert = userAlertRepository
                .findById(new UserAlertId(expectedLdap, expectedUserAlertId));

        assertTrue(retrievedUserAlert.isPresent());

        UserAlert actualUserAlert = retrievedUserAlert.get();

        assertEquals(expectedUserAlertId, actualUserAlert.getAlertId());
        assertEquals(expectedLdap, actualUserAlert.getLdap());
        assertEquals(expectedUpdateBy, actualUserAlert.getLastUpdateBy());
        assertTrue(actualUserAlert.getReadStatus());
    }

    @Test
    void updateAlertReadStatus_UserAlertIsNotExist_ThrowException() {
        Map<UUID, Boolean> expectedReadStatus = Map.of(
                UUID.fromString("15da03aa-357d-4a58-8b7d-809a7455ef70"), true
        );

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
                () -> alertService.updateAlertReadStatus("dxb87mu", "mc62ye", expectedReadStatus));

        String actualExceptionMessage = responseStatusException.getReason();

        assertNotNull(actualExceptionMessage);
        assertEquals("One or more alert not found.", actualExceptionMessage);
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

        alert.setKeyIdentifiers(mapper.convertValue(DEFAULT_KEY_IDENTIFIERS, JsonNode.class));
        alert.setTemplateBody(mapper.convertValue(DEFAULT_TEMPLATE, JsonNode.class));
        alert.setTemplateName("default");

        return alert;
    }

}
