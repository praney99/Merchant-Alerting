package com.homedepot.mm.pc.merchantalerting.domain;

import com.homedepot.mm.pc.merchantalerting.dao.AlertInfoDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<AlertInfoDAO, String> {

}

