package com.example.demo.dto;

import com.example.demo.model.UserRole;

public class AuthResponse {

    private String token;
    private Long userId;
    private String email;
    private UserRole role;

    public AuthResponse() {}

    public AuthResponse(String token, Long userId, String email, UserRole role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}
