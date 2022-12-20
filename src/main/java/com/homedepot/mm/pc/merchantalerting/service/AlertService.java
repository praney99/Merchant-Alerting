package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.*;

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
        Alert alert = request.toAlert();
        alert.setCreateBy("");
        alert.setCreateDate(new Date(System.currentTimeMillis()));

        List<UserAlert> userAlerts = new ArrayList<>();
        for (String ldap : ldapAssociations) {
            UserAlert ua = new UserAlert();
            ua.setAlertId(alert.getId());
            ua.setLdap(ldap);
            ua.setDismissDate(null);
            userAlerts.add(ua);
        }

        userAlertRepository.saveAll(userAlerts);
        return alertRepository.save(alert);
    }

    @Transactional
    public void deleteAlertByAlertId(UUID alertId) throws EmptyResultDataAccessException {
        alertRepository.deleteById(alertId);
    }

    public Optional<Alert> getAlert(UUID uuid) {
        return alertRepository.findById(uuid);
    }

    public List<Alert> getAlertsByLdap(String ldap) {
        return alertRepository.findAlertsByLdap(ldap);
    }

}
