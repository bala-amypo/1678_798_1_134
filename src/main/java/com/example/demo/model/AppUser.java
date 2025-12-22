package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String fullName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    // getters & setters
}
