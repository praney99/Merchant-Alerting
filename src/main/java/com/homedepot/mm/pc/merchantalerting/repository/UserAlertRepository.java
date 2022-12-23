package com.homedepot.mm.pc.merchantalerting.repository;

import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlert, UserAlertId> {

    @Query(value = "SELECT * FROM merch_alerts.user_alert WHERE alert_id = :id", nativeQuery = true)
    UserAlert findUserAlertById(@Param("id") UUID id);
}
