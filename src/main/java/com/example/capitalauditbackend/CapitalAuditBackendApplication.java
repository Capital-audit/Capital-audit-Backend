package com.example.capitalauditbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.example.capitalauditbackend.DOA.DatabaseConnector;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


import java.sql.Connection;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CapitalAuditBackendApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.setProperty("server.port", "8080");
        SpringApplication.run(CapitalAuditBackendApplication.class, args);
        DatabaseConnector db = DatabaseConnector.getInstance();
        db.connect();
    }
}

