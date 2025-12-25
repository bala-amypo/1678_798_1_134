package com.example.demo.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "recovery_curve_profiles", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"surgery_type", "day_number"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoveryCurveProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "surgery_type", nullable = false, length = 50)
    private String surgeryType; // KNEE, HIP, etc.
    
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber; // Days since surgery
    
    @Column(name = "expected_pain_level", nullable = false)
    private Integer expectedPainLevel; // 0-10
    
    @Column(name = "expected_mobility_level", nullable = false)
    private Integer expectedMobilityLevel; // 0-10
    
    @Column(name = "expected_fatigue_level", nullable = false)
    private Integer expectedFatigueLevel; // 0-10
    
    @Column(name = "min_pain_level")
    private Integer minPainLevel;
    
    @Column(name = "max_pain_level")
    private Integer maxPainLevel;
    
    @Column(name = "min_mobility_level")
    private Integer minMobilityLevel;
    
    @Column(name = "max_mobility_level")
    private Integer maxMobilityLevel;
    
    @Column(name = "min_fatigue_level")
    private Integer minFatigueLevel;
    
    @Column(name = "max_fatigue_level")
    private Integer maxFatigueLevel;
    
    @Column(name = "recovery_phase", length = 50)
    private String recoveryPhase; // ACUTE, SUBACUTE, CHRONIC
    
    @Column(length = 500)
    private String recommendations;
    
    @Column(name = "expected_activities", length = 1000)
    private String expectedActivities;
    
    @Column(name = "warning_signs", length = 1000)
    private String warningSigns;
    
    // Helper method to check if actual values are within expected range
    public boolean isPainWithinRange(Integer actualPain) {
        if (minPainLevel == null || maxPainLevel == null) return true;
        return actualPain >= minPainLevel && actualPain <= maxPainLevel;
    }
    
    public boolean isMobilityWithinRange(Integer actualMobility) {
        if (minMobilityLevel == null || maxMobilityLevel == null) return true;
        return actualMobility >= minMobilityLevel && actualMobility <= maxMobilityLevel;
    }
    
    public boolean isFatigueWithinRange(Integer actualFatigue) {
        if (minFatigueLevel == null || maxFatigueLevel == null) return true;
        return actualFatigue >= minFatigueLevel && actualFatigue <= maxFatigueLevel;
    }
    
    // Helper method to calculate expected wellness score
    public Double getExpectedWellnessScore() {
        double normalizedPain = (10 - expectedPainLevel) / 10.0;
        double normalizedMobility = expectedMobilityLevel / 10.0;
        double normalizedFatigue = (10 - expectedFatigueLevel) / 10.0;
        
        return (normalizedPain * 0.4) + (normalizedMobility * 0.4) + (normalizedFatigue * 0.2);
    }
}