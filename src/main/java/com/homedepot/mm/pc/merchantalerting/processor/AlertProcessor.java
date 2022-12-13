package com.homedepot.mm.pc.merchantalerting.processor;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.domain.RetrieveAlertResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homedepot.mm.pc.merchantalerting.dao.AlertInfoDAO;


import java.util.*;

@Service
@Slf4j
public class AlertProcessor {

    private AlertInfoDAO alertInfoDAO;

    @Autowired

    public AlertProcessor(AlertInfoDAO alertInfoDAO) {

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
}
