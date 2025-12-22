package com.example.demo.dto;

import com.example.demo.model.UserRole;
import jakarta.validation.constraints.*;

public class RegisterRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String fullName;

    private UserRole role;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public UserRole getRole() { return role; }
}
