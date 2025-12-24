package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "patient_profiles",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "patientId"),
        @UniqueConstraint(columnNames = "email")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String patientId;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @NotNull
    @Positive
    private Integer age;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String surgeryType;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
