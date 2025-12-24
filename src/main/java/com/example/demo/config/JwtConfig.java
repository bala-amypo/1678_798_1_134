package com.example.demo.config;

import com.example.demo.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    // Hardcoded constants (since properties cannot be used)
    private static final String JWT_SECRET =
            "7c5f8e2d1a4b9c3e6f7a2d8b5c3e1f4a9d2b5e8c1f4a7d0e3b6c9f2e5a8b1d4";

    private static final long JWT_EXPIRATION = 86400000L; // 24 hours

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(JWT_SECRET, JWT_EXPIRATION);
    }
}
