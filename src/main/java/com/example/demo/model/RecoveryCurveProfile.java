package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recovery_curve_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryCurveProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String surgeryType;

    @NotNull
    @Min(0)
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

    public RecoveryCurveProfile(String surgeryType, Integer dayNumber, Integer expectedPainLevel, Integer expectedMobilityLevel, Integer expectedFatigueLevel) {
        this.surgeryType = surgeryType;
        this.dayNumber = dayNumber;
        this.expectedPainLevel = expectedPainLevel;
        this.expectedMobilityLevel = expectedMobilityLevel;
        this.expectedFatigueLevel = expectedFatigueLevel;
    }
}