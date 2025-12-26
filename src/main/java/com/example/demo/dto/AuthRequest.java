package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication request containing user credentials")
public class AuthRequest {
    
    @Schema(description = "User email address", example = "user@example.com", required = true)
    private String email;
    
    @Schema(description = "User password", example = "password123", required = true)
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}