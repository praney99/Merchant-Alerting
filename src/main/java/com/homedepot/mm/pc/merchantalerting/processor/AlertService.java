package com.homedepot.mm.pc.merchantalerting.processor;

import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;

import io.micrometer.core.instrument.util.StringUtils;
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


    public List<Alert> retrieveAlertByUser(String userId) {

        List<Alert> retrieveAlertResponse;
        retrieveAlertResponse = alertInfoDAO.retrieveAlertDetails(userId);
        log.info("createAlertRequest: {}", userId);

        return retrieveAlertResponse;
    }

    public String deleteAlertByAlertId(UUID alertId) {
        if (!(StringUtils.isEmpty(alertId.toString()))) {
            try {
                alertInfoDAO.deleteById(alertId);
                return"The AlertId: "+alertId+" has been deleted";
                }
                    catch (EmptyResultDataAccessException erdae)
                    {
                    return ("The value {}  \nIs not correct:"+ alertId);
                    }
            } else {
                return ("AlertId is null");
            }

    }

}
