package com.homedepot.mm.pc.merchantalerting.repository;

import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlert, Long> {

    @Query(value = "SELECT * FROM merch_alerts.alert a JOIN merch_alerts.user_alert u ON a.id = u.alert_id WHERE u.alert_id = :id", nativeQuery = true)
    UserAlert findLdapById(@Param("id") UUID id);
}
