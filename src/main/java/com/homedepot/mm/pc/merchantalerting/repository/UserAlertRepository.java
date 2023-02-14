package com.homedepot.mm.pc.merchantalerting.repository;

import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlertId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlert, UserAlertId> {

    @Query(value = "select is_dismissed from merch_alerts.user_alert where ldap = :ldap", nativeQuery = true)
    Boolean findDismissStatus(@Param("ldap") String ldap);
}
