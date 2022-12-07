package com.homedepot.mm.pc.merchantalerting.processor;

import com.homedepot.mm.pc.merchantalerting.dao.AlertDAO;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.domain.RetrieveAlertResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.homedepot.mm.pc.merchantalerting.dao.AlertInfoDAO;


import java.util.*;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.NO_RECORDS_FOR_GIVEN_INPUT;

@Service
@Slf4j
public class AlertService {

    private AlertInfoDAO alertInfoDAO;
    private AlertDAO alertDAO;

    @Autowired

    public AlertService(AlertInfoDAO alertInfoDAO) {

        this.alertInfoDAO = alertInfoDAO;
    }


    public String createAlertByUser(CreateAlertRequest createAlertRequest)
    {
        String uuid = alertInfoDAO.createAlertDetails(createAlertRequest);
        log.info("createAlertRequest: {}", createAlertRequest);

        String createAlertResponse = uuid;

        return createAlertResponse;
    }


    public List<RetrieveAlertResponse> retrieveAlertByUser(String userId) {

        List<RetrieveAlertResponse> retrieveAlertResponse;
        retrieveAlertResponse = alertInfoDAO.retrieveAlertDetails(userId);
        log.info("createAlertRequest: {}", userId);

        return retrieveAlertResponse;
    }

    public void deleteAlertByAlertId(String alertId) {
        try {
            if (alertId.equals("") || alertId != null) {
                alertDAO.deleteById(alertId);
            } else {
                log.error("AlertId is null");
            }
        } catch (EmptyResultDataAccessException erdae) {
            log.error(NO_RECORDS_FOR_GIVEN_INPUT);
        }
    }

}
