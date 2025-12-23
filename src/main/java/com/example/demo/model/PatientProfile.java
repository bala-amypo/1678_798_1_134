package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PatientProfile {

    private Long id;
    private String patientId;
    private String fullName;
    private Integer age;
    private String email;
    private String surgeryType;
    private Boolean active;
    private LocalDateTime createdAt;
}
