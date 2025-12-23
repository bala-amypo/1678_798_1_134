package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recovery_curve_profiles")
public class RecoveryCurveProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String surgeryType;
    private int dayNumber;
    private double expectedValue;

    public RecoveryCurveProfile() {}

    /* ============== BUILDER ============== */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final RecoveryCurveProfile profile = new RecoveryCurveProfile();

        public Builder surgeryType(String surgeryType) {
            profile.setSurgeryType(surgeryType);
            return this;
        }

        public Builder dayNumber(int dayNumber) {
            profile.setDayNumber(dayNumber);
            return this;
        }

        public Builder expectedValue(double value) {
            profile.setExpectedValue(value);
            return this;
        }

        public RecoveryCurveProfile build() {
            return profile;
        }
    }

    /* ============== GETTERS / SETTERS ============== */

    public Long getId() {
        return id;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public double getExpectedValue() {
        return expectedValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public void setExpectedValue(double expectedValue) {
        this.expectedValue = expectedValue;
    }
}
