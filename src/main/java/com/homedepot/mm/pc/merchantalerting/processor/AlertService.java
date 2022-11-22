package com.homedepot.mm.pc.merchantalerting.processor;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homedepot.mm.pc.merchantalerting.dao.AlertInfoDAO;

@Service
@Slf4j
public class AlertService {

    private AlertInfoDAO alertInfoDAO;

    @Autowired
    public AlertService(AlertInfoDAO alertInfoDAO) {
        this.alertInfoDAO = alertInfoDAO;

    }

    public String createAlertByUser(CreateAlertRequest createAlertRequest) {
        String uuid = alertInfoDAO.getAlertInfo(createAlertRequest);
        log.info("createAlertRequest: {}", createAlertRequest);

        String createAlertResponse = uuid;

        return createAlertResponse;
    }
}
