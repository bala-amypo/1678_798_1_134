package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String patientId;

    private String fullName;
    private Integer age;

    @Column(unique = true)
    private String email;

    private String surgeryType;
    private Boolean active;
    private LocalDateTime createdAt;
}
