package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DatabaseConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("system"); // In real app, get from SecurityContext
    }

    // Configure H2 for development
    @Bean
    @Profile("dev")
    public org.h2.tools.Server h2TCPServer() throws java.sql.SQLException {
        return org.h2.tools.Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
    }

    // Additional Hibernate properties
    @Bean
    @Profile("!prod")
    public org.hibernate.cfg.AvailableSettings hibernateProperties() {
        return new org.hibernate.cfg.AvailableSettings() {};
    }
}