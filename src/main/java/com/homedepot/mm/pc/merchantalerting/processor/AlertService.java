package com.homedepot.mm.pc.merchantalerting.processor;

import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.NO_RECORDS_FOR_GIVEN_INPUT;

@Service
@Slf4j
public class AlertService {

    private AlertRepository alertRepository;
    private UserAlertRepository userAlertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository, UserAlertRepository userAlertRepository) {

        this.alertRepository = alertRepository;
        this.userAlertRepository= userAlertRepository;
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
        System.out.println(userAlertRepository.saveAll(userAlerts));
        System.out.println(alertRepository.save(alert));
        return alert;
    }
    public static String createAlertDetails(CreateAlertRequest createAlertRequest) {
        String results;
        try {
            CreateAlertRequest rsExtractorId1 = new CreateAlertRequest();
            results = null;
            return results;
        } catch (EmptyResultDataAccessException erdae) {
            log.error(NO_RECORDS_FOR_GIVEN_INPUT);
        }

        return null;
    }

    public List<Alert> retrieveAlertByUser(String userId) {

        List<Alert> retrieveAlertResponse;
        retrieveAlertResponse = retrieveAlertDetails(userId);
        log.info("createAlertRequest: {}", userId);

        return retrieveAlertResponse;
    }

    public static List<Alert> retrieveAlertDetails(String userId) {
        return null;
    }

    @Transactional
    public void deleteAlertByAlertId(UUID alertId) {
        try {
            alertRepository.deleteById(alertId);
        } catch (EmptyResultDataAccessException erdae) {
            log.error("Incorrect value "+ alertId);
            throw new EmptyResultDataAccessException(erdae.getMessage(), erdae.getExpectedSize());
        }
    }
    public Optional<Alert> getAlert(UUID uuid) {
        return alertRepository.findById(uuid);
    }

    public List<Alert> getAlertsByLdap(String ldap) {
        return alertRepository.findAlertsByLdap(ldap);
    }
    public Alert createAlertByLdap(CreateAlertRequest request, String ldap) {
        return createAlertWithLdapAssociations(request, List.of(ldap));
    }

}
