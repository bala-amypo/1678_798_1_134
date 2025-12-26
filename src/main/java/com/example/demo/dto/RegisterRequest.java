package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User registration request containing user details")
public class RegisterRequest {
    
    @Schema(description = "User email address", example = "newuser@example.com", required = true)
    private String email;
    
    @Schema(description = "User password", example = "securePassword123", required = true)
    private String password;
    
    @Schema(description = "User full name", example = "John Doe", required = true)
    private String fullName;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}