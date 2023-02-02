package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.domain.AlertTemplateType;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlertServiceTest {

    @InjectMocks
    AlertService alertService;

    @Mock
    AlertRepository alertRepository;

    @Mock
    UserAlertRepository userAlertRepository;


    @Test
    public void testCreateAlertHappyPath() {
        String ldap = "fo42br";

        CreateAlertRequest request = new CreateAlertRequest();
        request.setType("testType");
        request.setSystemSource("testSystemSource");
        request.setExpirationDate(null);
        request.setKeyIdentifiers(null);
        request.setTemplateName(AlertTemplateType.DEFAULT);

        Map<String, String> defaultTemplate = new HashMap<>();
        defaultTemplate.put("title","title");
        defaultTemplate.put("titleDescription","title description");
        defaultTemplate.put("primaryText1","primary text 1");
        defaultTemplate.put("primaryText2","primary text 2");
        defaultTemplate.put("tertiaryText","tertiary text");
        defaultTemplate.put("primaryLinkText","link");
        defaultTemplate.put("primaryLinkUri","http://localhost:8080");
        request.setTemplateBody(defaultTemplate);

        when(alertRepository.save(any())).thenReturn(new Alert());
        when(userAlertRepository.saveAll(any())).thenReturn(new ArrayList<>());

        Alert alert = alertService.createAlertByLdap(request, ldap);

        Assert.assertNotNull(alert);
    }

    @Test
    public void testCreateAlertWithCompleteAlertMapping() {
        String ldap = "fo42br";
        Calendar calendar = Calendar.getInstance();

        Map<String, String> keyIdentifiers = new HashMap<>();
        keyIdentifiers.put("cpi", "0.98");
        keyIdentifiers.put("sku", "123456");

        CreateAlertRequest request = new CreateAlertRequest();
        request.setType("testType");
        request.setSystemSource("testSystemSource");
        request.setExpirationDate(LocalDate.parse("2030-12-30"));
        request.setKeyIdentifiers(keyIdentifiers);
        request.setTemplateName(AlertTemplateType.DEFAULT);

        Map<String, String> defaultTemplate = new HashMap<>();
        defaultTemplate.put("title","title");
        defaultTemplate.put("titleDescription","title description");
        defaultTemplate.put("primaryText1","primary text 1");
        defaultTemplate.put("primaryText2","primary text 2");
        defaultTemplate.put("tertiaryText","tertiary text");
        defaultTemplate.put("primaryLinkText","link");
        defaultTemplate.put("primaryLinkUri","http://localhost:8080");
        request.setTemplateBody(defaultTemplate);

        when(alertRepository.save(any())).thenReturn(new Alert());
        when(userAlertRepository.saveAll(any())).thenReturn(new ArrayList<>());

        Alert alert = alertService.createAlertByLdap(request, ldap);

        Assert.assertNotNull(alert);
    }

    @Test
    public void testGetUserAlerts() {
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
    public void testDeleteAlert() {
        UUID alertId = UUID.randomUUID();
        doNothing().when(alertRepository).deleteById(any());
        alertService.deleteAlert(alertId);
    }

    @Test
    public void testExpireCronJob() {
        doNothing().when(alertRepository).deleteAlertsByExpirationDateBefore(any());
        alertService.cleanupExpiredAlerts();
        }

    @Test
    public void testDismissAlert() {
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
        alertService.dismissAlert(ldap, alertDismissalStates);

    }
}
