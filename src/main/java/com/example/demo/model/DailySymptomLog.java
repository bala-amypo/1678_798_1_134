package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_symptom_logs")
public class DailySymptomLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    private String symptomType;
    private Integer severity;
    private String notes;

    private LocalDate logDate;

    public DailySymptomLog() {}

    /* ========= BUILDER ========= */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final DailySymptomLog log = new DailySymptomLog();

        public Builder patientId(Long patientId) {
            log.setPatientId(patientId);
            return this;
        }

        public Builder symptomType(String symptomType) {
            log.setSymptomType(symptomType);
            return this;
        }

        public Builder severity(Integer severity) {
            log.setSeverity(severity);
            return this;
        }

        public Builder notes(String notes) {
            log.setNotes(notes);
            return this;
        }

        public Builder logDate(LocalDate logDate) {
            log.setLogDate(logDate);
            return this;
        }

        public DailySymptomLog build() {
            return log;
        }
    }

    /* ========= GETTERS ========= */

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getSymptomType() {
        return symptomType;
    }

    public Integer getSeverity() {
        return severity;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    /* ========= SETTERS ========= */

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public void setSymptomType(String symptomType) {
        this.symptomType = symptomType;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }
}
