package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserAlertRepository userAlertRepository;
    private final UserMatrixService userMatrixService;


    @Autowired
    public AlertService(AlertRepository alertRepository, UserAlertRepository userAlertRepository, UserMatrixService userMatrixService) {
        this.alertRepository = alertRepository;
        this.userAlertRepository = userAlertRepository;
        this.userMatrixService = userMatrixService;
    }

    @Transactional
    public Alert createAlertByLdap(CreateAlertRequest request, String ldap) {
        return createAlertWithLdapAssociations(request, List.of(ldap));
    }

    @Transactional
    public Alert createAlertByDCS(CreateAlertRequest request, String dcs) {
        List<String> userIds = userMatrixService.getUserLDAPForGivenDCS(dcs);

        if (userIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input DCS does not have any associated users.");
        }
        return createAlertWithLdapAssociations(request, userIds);
    }

    private Alert createAlertWithLdapAssociations(CreateAlertRequest request, List<String> ldapAssociations) {
        Alert alert = request.toAlert();
        Alert persistedAlert = alertRepository.save(alert);

        List<UserAlert> userAlerts = new ArrayList<>();
        for (String ldap : ldapAssociations) {
            UserAlert ua = new UserAlert(ldap, persistedAlert.getId());
            userAlerts.add(ua);
        }
        userAlertRepository.saveAll(userAlerts);

        return persistedAlert;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Alert> getAlertsByLdap(String ldap) {
        return alertRepository.findAlertsByLdap(ldap);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Alert> getAlert(UUID uuid) {
        return alertRepository.findById(uuid);
    }

    @Transactional
    public void deleteAlert(UUID alertId) {
        alertRepository.deleteById(alertId);
    }

    @Transactional
    public void dismissAlert(String ldap, UUID alertId) {
        UserAlertId id = new UserAlertId(ldap, alertId);
        Optional<UserAlert> optionalUserAlert = userAlertRepository.findById(id);

        if (optionalUserAlert.isPresent()) {
            UserAlert userAlert = optionalUserAlert.get();
            userAlert.setIsDismissed(true);
            userAlert.setLastUpdated(new Timestamp(System.currentTimeMillis()));
            userAlert.setLastUpdateBy(ldap); // Should this value come from the system making this call? Right now this assumes only users will dismiss their own alerts.
            userAlertRepository.save(userAlert);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error dismissing alert " + alertId + " for user " + ldap + ".");
        }
    }
}
