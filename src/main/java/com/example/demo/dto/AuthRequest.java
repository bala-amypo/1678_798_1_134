package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class AuthRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
