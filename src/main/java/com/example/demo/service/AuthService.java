package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public AuthResponse register(RegisterRequest r) {

        // Fake register logic (for demo)
        String username = r.getUsername();
        String role = r.getRole();

        return new AuthResponse(
                "User registered successfully",
                username,
                role
        );
    }

    public AuthResponse login(String username) {
        return new AuthResponse(
                "Login successful",
                username,
                "USER"
        );
    }
}
