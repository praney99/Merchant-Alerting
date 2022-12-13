package com.homedepot.mm.pc.merchantalerting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import com.homedepot.mm.pc.merchantalerting.template.DefaultTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
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
        request.setTemplateName("default");

        DefaultTemplate defaultTemplate = new DefaultTemplate();
        defaultTemplate.setTitle("title");
        defaultTemplate.setTitleDescription("title description");
        defaultTemplate.setPrimaryText1("primary text 1");
        defaultTemplate.setPrimaryText2("primary text 2");
        defaultTemplate.setTertiaryText("tertiary text");
        defaultTemplate.setPrimaryLinkText("link");
        defaultTemplate.setPrimaryLinkUri("http://localhost:8080");

        request.setTemplateBody(new ObjectMapper().convertValue(defaultTemplate, HashMap.class));

        when(alertRepository.save(any())).thenReturn(new Alert());
        when(userAlertRepository.saveAll(any())).thenReturn(new ArrayList<>());

        Alert alert = alertService.createAlertByLdap(request, ldap);

        Assert.assertNotNull(alert);
    }

    @Test
    public void testCreateAlertWithCompleteAlertMapping() {
        String ldap = "fo42br";
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTime().getTime());

        Map<String, String> keyIdentifiers = new HashMap<>();
        keyIdentifiers.put("cpi", "0.98");
        keyIdentifiers.put("sku", "123456");

        CreateAlertRequest request = new CreateAlertRequest();
        request.setType("testType");
        request.setSystemSource("testSystemSource");
        request.setExpirationDate(Long.toString(date.getTime()));
        request.setKeyIdentifiers(keyIdentifiers);
        request.setTemplateName("default");

        DefaultTemplate defaultTemplate = new DefaultTemplate();
        defaultTemplate.setTitle("title");
        defaultTemplate.setTitleDescription("title description");
        defaultTemplate.setPrimaryText1("primary text 1");
        defaultTemplate.setPrimaryText2("primary text 2");
        defaultTemplate.setTertiaryText("tertiary text");
        defaultTemplate.setPrimaryLinkText("link");
        defaultTemplate.setPrimaryLinkUri("http://localhost:8080");
        request.setTemplateBody(new ObjectMapper().convertValue(defaultTemplate, HashMap.class));

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

        List<Alert> response = alertService.getAlertsForUser(ldap);
        Assert.assertEquals(response.get(0).getId(), alertId);
    }
}
