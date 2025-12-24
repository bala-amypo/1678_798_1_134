package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    
    private String secret;
    private Long expiration;
    private String header;
    private String prefix;
    
    @PostConstruct
    protected void init() {
        // Ensure secret is Base64 encoded and of sufficient length for HS512
        if (secret == null || secret.length() < 64) {
            throw new IllegalStateException(
                "JWT secret must be at least 64 characters long. " +
                "Configure it in application.properties as app.jwt.secret"
            );
        }
        
        // Ensure secret is Base64 encoded
        try {
            Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "JWT secret must be Base64 encoded. " +
                "You can generate one with: openssl rand -base64 64"
            );
        }
        
        // Set defaults if not provided
        if (header == null) {
            header = "Authorization";
        }
        
        if (prefix == null) {
            prefix = "Bearer ";
        }
        
        if (expiration == null) {
            expiration = 86400000L; // 24 hours in milliseconds
        }
    }
}