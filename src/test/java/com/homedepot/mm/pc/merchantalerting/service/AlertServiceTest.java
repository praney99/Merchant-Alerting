package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.domain.AlertTemplateType;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.entity.Alert;
import com.homedepot.mm.pc.merchantalerting.entity.UserAlert;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.homedepot.mm.pc.merchantalerting.TestUtils.DEFAULT_KEY_IDENTIFIERS;
import static com.homedepot.mm.pc.merchantalerting.TestUtils.DEFAULT_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private UserAlertRepository userAlertRepository;

    @InjectMocks
    private AlertService alertService;

    @Captor
    private ArgumentCaptor<Date> dateCaptor;

    @Captor
    private ArgumentCaptor<List<UserAlert>> userAlertsCaptor;

    @Test
    void testCreateAlertHappyPath() {
        String ldap = "fo42br";

        CreateAlertRequest request = new CreateAlertRequest();
        request.setType("testType");
        request.setSystemSource("testSystemSource");
        request.setExpirationDate(null);
        request.setKeyIdentifiers(null);
        request.setTemplateName(AlertTemplateType.DEFAULT);

        request.setTemplateBody(DEFAULT_TEMPLATE);

        when(alertRepository.save(any())).thenReturn(new Alert());
        when(userAlertRepository.saveAll(any())).thenReturn(new ArrayList<>());

        Alert alert = alertService.createAlertByLdap(request, ldap);

        assertNotNull(alert);
    }

    @Test
    void testCreateAlertWithCompleteAlertMapping() {
        String ldap = "fo42br";
        Calendar calendar = Calendar.getInstance();


        CreateAlertRequest request = new CreateAlertRequest();
        request.setType("testType");
        request.setSystemSource("testSystemSource");
        request.setExpirationDate(LocalDate.parse("2030-12-30"));
        request.setKeyIdentifiers(DEFAULT_KEY_IDENTIFIERS);
        request.setTemplateName(AlertTemplateType.DEFAULT);

        request.setTemplateBody(DEFAULT_TEMPLATE);

        when(alertRepository.save(any())).thenReturn(new Alert());
        when(userAlertRepository.saveAll(any())).thenReturn(new ArrayList<>());

        Alert alert = alertService.createAlertByLdap(request, ldap);

        assertNotNull(alert);
    }

    @Test
    void testGetUserAlerts() {
        String ldap = "fo42br";
        UUID alertId = UUID.fromString("15da03aa-357d-4a58-8b7d-809a7455ef70");

        Alert alert = new Alert();
        alert.setId(alertId);
        alert.setCreateBy("test-method");

        List<Alert> alerts = new ArrayList<>();
        alerts.add(alert);

        when(alertRepository.findAlertsByLdap(any()))
                .thenReturn(alerts);

        List<Alert> response = alertService.getAlertsByLdap(ldap);
        assertEquals(response.get(0).getId(), alertId);
    }

    @Test
    void testDeleteAlert() {
        UUID alertId = UUID.randomUUID();
        doNothing().when(alertRepository).deleteById(any());
        alertService.deleteAlert(alertId);
    }

    @Test
    void testExpireCronJob() {
        Date expectedDate = Date.valueOf(LocalDate.now());

        when(alertRepository.deleteAlertsByExpirationDateBefore(dateCaptor.capture()))
                .thenReturn(Lists.emptyList());

        alertService.cleanupExpiredAlerts();

        assertEquals(expectedDate, dateCaptor.getValue());

        verify(alertRepository).deleteAlertsByExpirationDateBefore(any());

        verifyNoMoreInteractions(alertRepository);
        verifyNoInteractions(userAlertRepository);
    }

    @Test
    void testDismissAlert() {
        Map<UUID, Boolean> alertDismissalStates = new HashMap<>();
        UUID alertId_0 = UUID.randomUUID();
        UUID alertId_1 = UUID.randomUUID();
        alertDismissalStates.put(alertId_0, true);
        alertDismissalStates.put(alertId_1, false);
        String ldap = "user0";
        UserAlert userAlert_0 = new UserAlert(ldap, alertId_0);
        UserAlert userAlert_1 = new UserAlert(ldap, alertId_1);
        List<UserAlert> userAlerts = List.of(userAlert_0, userAlert_1);
        when(userAlertRepository.findAllById(any()))
                .thenReturn(userAlerts);
        when(userAlertRepository.saveAll(any()))
                .thenReturn(userAlerts);
        alertService.dismissAlert(ldap, "user0", alertDismissalStates);

    }

    @Test
    void updateAlertReadStatus_UserAlertIsExist_UpdateReadStatusAndLastUpdate() {
        String expectedLdap = "dxb87mu";
        String expectedUpdateBy = "mc62ye";
        UUID expectedUserAlertId = UUID.fromString("15da03aa-357d-4a58-8b7d-809a7455ef70");

        UserAlert expectedUserAlert = new UserAlert(expectedLdap, expectedUserAlertId);
        expectedUserAlert.setReadStatus(false);

        List<UserAlert> expectedUserAlerts = List.of(expectedUserAlert);

        Map<UUID, Boolean> expectedReadStatus = Map.of(
                expectedUserAlertId, true
        );

        when(userAlertRepository.findAllById(any()))
                .thenReturn(expectedUserAlerts);

        when(userAlertRepository.saveAll(userAlertsCaptor.capture()))
                .thenReturn(expectedUserAlerts);

        alertService.updateAlertReadStatus(expectedLdap, expectedUpdateBy, expectedReadStatus);

        List<UserAlert> actualUserAlerts = userAlertsCaptor.getValue();

        assertNotNull(actualUserAlerts);
        assertEquals(expectedUserAlerts.size(), actualUserAlerts.size());

        UserAlert actualUserAlert = actualUserAlerts.get(0);

        assertEquals(expectedUserAlert.getLdap(), actualUserAlert.getLdap());
        assertEquals(expectedUserAlert.getAlertId(), actualUserAlert.getAlertId());
        assertEquals(expectedLdap, actualUserAlert.getLdap());
        assertEquals(expectedUpdateBy, actualUserAlert.getLastUpdateBy());
        assertTrue(actualUserAlert.getReadStatus());

        verify(userAlertRepository).findAllById(any());
        verify(userAlertRepository).saveAll(any());

        verifyNoMoreInteractions(userAlertRepository);
        verifyNoInteractions(alertRepository);
    }

    @Test
    void updateAlertReadStatus_UserAlertIsNotExist_ThrowException() {
        Map<UUID, Boolean> expectedReadStatus = Map.of(
                UUID.fromString("15da03aa-357d-4a58-8b7d-809a7455ef70"), true
        );

        when(userAlertRepository.findAllById(any()))
                .thenReturn(Lists.emptyList());

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
                () -> alertService.updateAlertReadStatus("dxb87mu", "mc62ye", expectedReadStatus));

        String actualExceptionMessage = responseStatusException.getReason();

        assertNotNull(actualExceptionMessage);
        assertEquals("One or more alert not found.", actualExceptionMessage);

        verify(userAlertRepository).findAllById(any());

        verifyNoMoreInteractions(userAlertRepository);
        verifyNoInteractions(alertRepository);
    }
}
