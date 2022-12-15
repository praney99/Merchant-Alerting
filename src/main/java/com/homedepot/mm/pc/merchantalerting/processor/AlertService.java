package com.homedepot.mm.pc.merchantalerting.processor;

import com.homedepot.mm.pc.merchantalerting.domain.Alert;
import com.homedepot.mm.pc.merchantalerting.domain.model.AlertInfo;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.homedepot.mm.pc.merchantalerting.dao.AlertInfoDAO;


import java.util.*;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.ALERT_DELETED;
import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.NO_RECORDS_FOR_GIVEN_INPUT;

@Service
@Slf4j
public class AlertService {

    private AlertInfo alertInfo;
    private AlertInfoDAO alertInfoDAO;

    @Autowired
    public AlertService(AlertInfo alertInfo) {

        this.alertInfo = alertInfo;
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
            try {
                alertInfo.deleteById(alertId);
                return ALERT_DELETED;
                }
                    catch (EmptyResultDataAccessException erdae)
                    {
                        log.error("Incorrect value" + alertId);
                        return ("Incorrect value of alert id");
                    }

    }

}
