package com.homedepot.mm.pc.merchantalerting.configuration;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@Profile({"default", "cloud", "dev"})
public class PostgresConfiguration {

    public PGSimpleDataSource getPostgresDataSource() {
        String postgresHost = System.getenv("PSQL_HOST");
        String postgresPort = System.getenv("PSQL_PORT");
        String postgresUser = System.getenv("PSQL_USER");
        String postgresDatabase = System.getenv("DATABASE");
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        PGSimpleDataSource postgresDataSource = new PGSimpleDataSource();

        postgresDataSource.setUser(username);
        postgresDataSource.setPassword(password);
        postgresDataSource.setUrl(String.format("jdbc:postgresql://%s:%s/%s", postgresHost, postgresPort, postgresUser));

        return postgresDataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterPostgresJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getPostgresDataSource());
    }
}