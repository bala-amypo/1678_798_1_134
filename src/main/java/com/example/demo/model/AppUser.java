package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppUser {

    private Long id;
    private String email;
    private String password;
    private String fullName;
    private UserRole role;
}
