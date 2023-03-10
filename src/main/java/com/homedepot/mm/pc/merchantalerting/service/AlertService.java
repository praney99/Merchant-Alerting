package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

    private final AlertRepository alertRepository;
    private final UserAlertRepository userAlertRepository;
    private final UserMatrixService userMatrixService;


    @Autowired
    public AlertService(AlertRepository alertRepository, UserAlertRepository userAlertRepository, UserMatrixService userMatrixService) {
        this.alertRepository = alertRepository;
        this.userAlertRepository = userAlertRepository;
        this.userMatrixService = userMatrixService;
    }

    /**
     * CRON JOB - Scheduled to run every day at midnight.
     *
     * Deletes from the database Alerts with expiration dates prior to today's date.
     * Cascading delete functionality will also delete associated UserAlerts.
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredAlerts() {
        LOGGER.warn("Running Cleanup Job for Expired Alert...");
        Date todaysDate = Date.valueOf(LocalDate.now());
        List<Alert> deletedAlerts = alertRepository.deleteAlertsByExpirationDateBefore(todaysDate);
        List<UUID> deletedAlertIds = deletedAlerts.stream()
                .map(Alert::getId)
                .collect(Collectors.toList());
        LOGGER.warn("Removed the following expired alerts: " + deletedAlertIds);
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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void dismissAlert(String ldap, String updatedBy, Map<UUID, Boolean> alertDismissalStates) {
        List<UserAlertId> userAlertIds = new ArrayList<>();

        alertDismissalStates.keySet().forEach(alertId ->  {
            UserAlertId id = new UserAlertId(ldap, alertId);
            userAlertIds.add(id);
        });

        List<UserAlert> userAlerts = userAlertRepository.findAllById(userAlertIds);

        if (userAlerts.size() != userAlertIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error dismissing alerts. One or more alert not found.");
        }

        for (UserAlert userAlert : userAlerts) {
            Boolean isDismissed = alertDismissalStates.get(userAlert.getAlertId());
            userAlert.setIsDismissed(isDismissed);
            userAlert.setLastUpdated(new Timestamp(System.currentTimeMillis()));
            userAlert.setLastUpdateBy(updatedBy);
        }

        userAlertRepository.saveAll(userAlerts);
    }
}
