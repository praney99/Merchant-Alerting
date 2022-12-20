package com.homedepot.mm.pc.merchantalerting.processor;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.domain.RetrieveAlertResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homedepot.mm.pc.merchantalerting.dao.AlertInfoDAO;


import java.util.*;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.NO_RECORDS_FOR_GIVEN_INPUT;

@Service
@Slf4j
public class AlertService {

    private AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository) {

        this.alertRepository = alertRepository;
    }


    public String createAlertByUser(CreateAlertRequest createAlertRequest)
    {
        String uuid = createAlertDetails(createAlertRequest);
        log.info("createAlertRequest: {}", createAlertRequest);

        String createAlertResponse = uuid;

        return createAlertResponse;
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
}
