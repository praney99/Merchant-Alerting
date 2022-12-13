package com.homedepot.mm.pc.merchantalerting.repository;

import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlert, Long> {

    List<UserAlert> findAllByLdap(String ldap);
}
