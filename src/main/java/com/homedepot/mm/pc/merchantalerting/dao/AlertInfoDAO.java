package com.homedepot.mm.pc.merchantalerting.dao;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.processor.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.*;

@Repository
@Slf4j
public class AlertInfoDAO {

    public String getAlertInfo(CreateAlertRequest createAlertRequest) {
        String results;
        try {
            CreateAlertRequest rsExtractorId = new CreateAlertRequest();
            results = null;
            return results;
        } catch (EmptyResultDataAccessException erdae) {
            log.error(NO_RECORDS_FOR_GIVEN_INPUT);
        }
            return null;
    }
}
