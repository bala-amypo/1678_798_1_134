package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class RecoveryCurveProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String surgeryType;
    private Integer dayNumber;
    private Integer expectedPainLevel;
    private Integer expectedMobilityLevel;
    private Integer expectedFatigueLevel;

    public RecoveryCurveProfile() {
    }

    public RecoveryCurveProfile(Long id, String surgeryType, Integer dayNumber, Integer expectedPainLevel, Integer expectedMobilityLevel, Integer expectedFatigueLevel) {
        this.id = id;
        this.surgeryType = surgeryType;
        this.dayNumber = dayNumber;
        this.expectedPainLevel = expectedPainLevel;
        this.expectedMobilityLevel = expectedMobilityLevel;
        this.expectedFatigueLevel = expectedFatigueLevel;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecoveryCurveProfile that = (RecoveryCurveProfile) o;
        return Objects.equals(id, that.id) && Objects.equals(surgeryType, that.surgeryType) && Objects.equals(dayNumber, that.dayNumber) && Objects.equals(expectedPainLevel, that.expectedPainLevel) && Objects.equals(expectedMobilityLevel, that.expectedMobilityLevel) && Objects.equals(expectedFatigueLevel, that.expectedFatigueLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surgeryType, dayNumber, expectedPainLevel, expectedMobilityLevel, expectedFatigueLevel);
    }

    @Override
    public String toString() {
        return "RecoveryCurveProfile{" +
                "id=" + id +
                ", surgeryType='" + surgeryType + '\'' +
                ", dayNumber=" + dayNumber +
                ", expectedPainLevel=" + expectedPainLevel +
                ", expectedMobilityLevel=" + expectedMobilityLevel +
                ", expectedFatigueLevel=" + expectedFatigueLevel +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String surgeryType;
        private Integer dayNumber;
        private Integer expectedPainLevel;
        private Integer expectedMobilityLevel;
        private Integer expectedFatigueLevel;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder surgeryType(String surgeryType) {
            this.surgeryType = surgeryType;
            return this;
        }

        public Builder dayNumber(Integer dayNumber) {
            this.dayNumber = dayNumber;
            return this;
        }

        public Builder expectedPainLevel(Integer expectedPainLevel) {
            this.expectedPainLevel = expectedPainLevel;
            return this;
        }

        public Builder expectedMobilityLevel(Integer expectedMobilityLevel) {
            this.expectedMobilityLevel = expectedMobilityLevel;
            return this;
        }

        public Builder expectedFatigueLevel(Integer expectedFatigueLevel) {
            this.expectedFatigueLevel = expectedFatigueLevel;
            return this;
        }

        public RecoveryCurveProfile build() {
            return new RecoveryCurveProfile(id, surgeryType, dayNumber, expectedPainLevel, expectedMobilityLevel, expectedFatigueLevel);
        }
    }
}