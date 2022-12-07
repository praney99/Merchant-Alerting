package com.homedepot.mm.pc.merchantalerting.dao;


import com.homedepot.mm.pc.merchantalerting.domain.RetrieveAlertResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertDAO extends JpaRepository<RetrieveAlertResponse, UUID> {
}
