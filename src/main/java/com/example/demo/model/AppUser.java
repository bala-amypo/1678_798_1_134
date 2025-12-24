package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "app_users",
       uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String fullName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;
}
