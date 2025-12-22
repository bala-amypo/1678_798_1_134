package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class AuthRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    // getters & setters
}
