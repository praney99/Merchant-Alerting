package com.homedepot.mm.pc.merchantalerting;

import com.homedepot.mm.pc.merchantalerting.repository.AlertRepository;
import com.homedepot.mm.pc.merchantalerting.repository.UserAlertRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class PostgresContainerBaseTest {
    static PostgreSQLContainer<?> postgreSQLContainer;

    @Autowired
    protected AlertRepository alertRepository;

    @Autowired
    protected UserAlertRepository userAlertRepository;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

        postgreSQLContainer.withStartupTimeout(Duration.ofMinutes(1))
                .waitingFor(Wait.forLogMessage(".*database system is ready.*\\n", 1));
        postgreSQLContainer.start();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
        System.setProperty("spring.jpa.properties.hibernate.hbm2dll.create_namespaces", "true");
        System.setProperty("spring.jpa.hibernate.ddl-auto", "update");
    }

    @AfterEach
    protected void cleanup() {
        alertRepository.deleteAll();
        userAlertRepository.deleteAll();
    }
}
