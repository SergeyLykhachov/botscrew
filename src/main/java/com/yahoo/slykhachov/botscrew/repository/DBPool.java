package com.yahoo.slykhachov.botscrew.repository;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class DBPool {

    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.username}")
    private String DB_USR_NAME;

    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;

    private HikariDataSource hikariDataSource;

    @PostConstruct
    private void init() {
        this.hikariDataSource = new HikariDataSource();
        this.hikariDataSource.setJdbcUrl(DB_URL);
        this.hikariDataSource.setUsername(DB_USR_NAME);
        this.hikariDataSource.setPassword(DB_PASSWORD);
        this.hikariDataSource.setMinimumIdle(100);
        this.hikariDataSource.setMaximumPoolSize(1);
        this.hikariDataSource.setAutoCommit(false);
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

}

