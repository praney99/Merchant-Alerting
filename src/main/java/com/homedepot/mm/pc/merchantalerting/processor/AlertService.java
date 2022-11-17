package com.homedepot.mm.pc.merchantalerting.processor;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homedepot.mm.pc.merchantalerting.dao.AlertInfoDAO;

import java.sql.ResultSet;

@Service
    @Slf4j
    public class AlertService {

        private static AlertInfoDAO alertInfoDAO = new AlertInfoDAO();

        @Autowired
        public AlertService(AlertInfoDAO alertInfoDAO) {
            this.alertInfoDAO = alertInfoDAO;

        }

    public static String getIdDetails(CreateAlertRequest createAlertRequest) {
        String uuid = alertInfoDAO.getAlertInfo(createAlertRequest);
        log.info("createAlertRequest: {}", createAlertRequest);

        String createAlertResponse = uuid;

        return createAlertResponse;
    }
}
