package com.homedepot.mm.pc.merchantalerting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;

public interface UserAlertRepository extends JpaRepository<UserAlert,Long>{
}
