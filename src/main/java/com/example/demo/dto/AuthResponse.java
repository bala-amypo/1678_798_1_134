package com.example.demo.dto;

public class AuthResponse {
    private String email;
    private String token;

    public AuthResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() { return email; }
    public String getToken() { return token; }
}
