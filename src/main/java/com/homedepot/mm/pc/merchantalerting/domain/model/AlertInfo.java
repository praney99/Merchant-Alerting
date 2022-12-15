package com.homedepot.mm.pc.merchantalerting.domain.model;

import com.homedepot.mm.pc.merchantalerting.domain.Alert;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.homedepot.mm.pc.merchantalerting.constants.ErrorConstants.NO_RECORDS_FOR_GIVEN_INPUT;

@Repository
public interface AlertInfo extends JpaRepository<Alert, UUID> {

}
