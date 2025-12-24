package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "clinical_alert_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalAlertRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long patientId;

    @NotNull
    private Long logId;

    @NotBlank
    private String alertType;

    @NotBlank
    private String severity;

    @Lob
    @NotBlank
    private String message;

    @Column(nullable = false)
    private Boolean resolved = false;

    @CreationTimestamp
    private LocalDate alertDate;
}
