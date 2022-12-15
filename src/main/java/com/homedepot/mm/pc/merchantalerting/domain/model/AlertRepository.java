package com.homedepot.mm.pc.merchantalerting.domain.model;

import com.homedepot.mm.pc.merchantalerting.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {

}
