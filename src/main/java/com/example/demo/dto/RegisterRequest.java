package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String fullName;

    @NotNull
    private UserRole role;

    // getters & setters
}
