package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
    name = "recovery_curve_profiles",
    uniqueConstraints = @UniqueConstraint(columnNames = {"surgeryType", "dayNumber"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryCurveProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String surgeryType;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer dayNumber;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer expectedPainLevel;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer expectedMobilityLevel;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer expectedFatigueLevel;
}
