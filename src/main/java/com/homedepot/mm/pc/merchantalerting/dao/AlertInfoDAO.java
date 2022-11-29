package com.homedepot.mm.pc.merchantalerting.dao;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;

import com.homedepot.mm.pc.merchantalerting.domain.RetrieveAlertResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.*;

@Repository
@Slf4j
public class AlertInfoDAO {


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

    public List<RetrieveAlertResponse> retrieveAlertDetails(String userId) {
        return null;

        /*(ResultSet rs) -> {
            List<RetrieveAlertResponse> results = null;
            while (rs.next()) {
                results.add(RetrieveAlertResponse.builder()
                        .id()
                        .keyIdentifiers()
                        .systemSource()
                        .type()
                        .templateName()
                        .templateBody()
                        .createDate()
                        .createdBy()
                        .build());
            }
        };*/

    }
}
