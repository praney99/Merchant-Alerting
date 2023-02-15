package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.domain.AlertTemplateType;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.entity.Alert;
import com.homedepot.mm.pc.merchantalerting.entity.UserAlert;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.homedepot.mm.pc.merchantalerting.TestUtils.DEFAULT_KEY_IDENTIFIERS;
import static com.homedepot.mm.pc.merchantalerting.TestUtils.DEFAULT_TEMPLATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private UserAlertRepository userAlertRepository;

    @InjectMocks
    private AlertService alertService;

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

        Assert.assertNotNull(alert);
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

        Assert.assertNotNull(alert);
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
        Assert.assertEquals(response.get(0).getId(), alertId);
    }

    @Test
    void testDeleteAlert() {
        UUID alertId = UUID.randomUUID();
        doNothing().when(alertRepository).deleteById(any());
        alertService.deleteAlert(alertId);
    }

    @Test
    void testExpireCronJob() {
        doNothing().when(alertRepository).deleteAlertsByExpirationDateBefore(any());
        alertService.cleanupExpiredAlerts();
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
}
