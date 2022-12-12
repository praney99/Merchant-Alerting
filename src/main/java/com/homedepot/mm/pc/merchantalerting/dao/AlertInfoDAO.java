package com.homedepot.mm.pc.merchantalerting.dao;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;

import com.homedepot.mm.pc.merchantalerting.model.Alert;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.*;

@Repository
@Slf4j
public abstract class AlertInfoDAO implements JpaRepository <Alert, UUID>{


    public String createAlertDetails(CreateAlertRequest createAlertRequest) {
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

    public List<Alert> retrieveAlertDetails(String userId) {
        return null;
    }

}
