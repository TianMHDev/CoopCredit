package com.coopcredit.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for Credit Application Service.
 * This microservice implements the Affiliate Management module using Hexagonal
 * Architecture.
 */
@SpringBootApplication
public class CreditApplicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditApplicationServiceApplication.class, args);
    }
}
