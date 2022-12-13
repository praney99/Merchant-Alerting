package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import com.homedepot.mm.pc.merchantalerting.util.AlertValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

    private final AlertRepository alertRepository;
    private final UserAlertRepository userAlertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository, UserAlertRepository userAlertRepository) {
        this.alertRepository = alertRepository;
        this.userAlertRepository = userAlertRepository;
    }

    public Alert createAlertByLdap(CreateAlertRequest request, String ldap) {
        return createAlertWithLdapAssociations(request, List.of(ldap));
    }

    @Transactional
    public Alert createAlertWithLdapAssociations(CreateAlertRequest request, List<String> ldapAssociations) {
        AlertValidator.validateAlertRequest(request);

        Alert alert = new Alert();
        alert.setCreateBy("");
        alert.setCreateDate(new Date(System.currentTimeMillis()));
        mapCreateAlertRequestToAlert(request, alert);

        List<UserAlert> userAlerts = new ArrayList<>();
        for (String ldap : ldapAssociations) {
            UserAlert ua = new UserAlert();
            ua.setAlertId(alert.getId());
            ua.setLdap(ldap);
            ua.setDismissDate(null);
            userAlerts.add(ua);
        }

        Alert savedAlert = alertRepository.save(alert);
        userAlertRepository.saveAll(userAlerts);

        return savedAlert;
    }

    public Optional<Alert> getAlert(UUID uuid) {
        return alertRepository.findById(uuid);
    }

    public List<Alert> getAlertsByLdap(String ldap) {
        return alertRepository.findAlertsByLdap(ldap);
    }

    private void mapCreateAlertRequestToAlert(CreateAlertRequest request, Alert alert) {
        alert.setKeyIdentifiers(request.getKeyIdentifiers() == null ? null : request.getKeyIdentifiers().toString());
        alert.setSystemSource(request.getSystemSource());
        alert.setAlertType(request.getType());
        alert.setTemplateName(request.getTemplateName());
        alert.setTemplateBody(request.getTemplateBody().toString());
        alert.setExpirationDate(request.getExpirationDate() == null ? null : new Date(Long.parseLong(request.getExpirationDate())));
    }

}
