package com.example.demo.model;

import java.time.LocalDateTime;

/**
 * Plain Java model
 * - NO Lombok
 * - NO Swagger
 * - NO JWT
 * - NO validation
 * - Safe for Spring Boot 3 compilation
 */
public class RecoveryCurveProfile {

    private Long id;
    private String surgeryType;
    private Integer dayNumber;
    private Integer expectedPainLevel;
    private Integer expectedMobilityLevel;
    private Integer expectedFatigueLevel;
    private LocalDateTime createdAt;

    // ---------- No-Args Constructor ----------
    public RecoveryCurveProfile() {
    }

    // ---------- All-Args Constructor ----------
    public RecoveryCurveProfile(
            Long id,
            String surgeryType,
            Integer dayNumber,
            Integer expectedPainLevel,
            Integer expectedMobilityLevel,
            Integer expectedFatigueLevel,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.surgeryType = surgeryType;
        this.dayNumber = dayNumber;
        this.expectedPainLevel = expectedPainLevel;
        this.expectedMobilityLevel = expectedMobilityLevel;
        this.expectedFatigueLevel = expectedFatigueLevel;
        this.createdAt = createdAt;
    }

    // ---------- Getters & Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Integer getExpectedPainLevel() {
        return expectedPainLevel;
    }

    public void setExpectedPainLevel(Integer expectedPainLevel) {
        this.expectedPainLevel = expectedPainLevel;
    }

    public Integer getExpectedMobilityLevel() {
        return expectedMobilityLevel;
    }

    public void setExpectedMobilityLevel(Integer expectedMobilityLevel) {
        this.expectedMobilityLevel = expectedMobilityLevel;
    }

    public Integer getExpectedFatigueLevel() {
        return expectedFatigueLevel;
    }

    public void setExpectedFatigueLevel(Integer expectedFatigueLevel) {
        this.expectedFatigueLevel = expectedFatigueLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
