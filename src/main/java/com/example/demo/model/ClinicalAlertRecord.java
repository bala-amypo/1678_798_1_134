package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_alert_records")
public class ClinicalAlertRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private String message;
    private String severity;
    private String alertType;
    private boolean resolved;
    private LocalDateTime createdAt;

    public ClinicalAlertRecord() {}

    /* ========= BUILDER ========= */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ClinicalAlertRecord record = new ClinicalAlertRecord();

        public Builder patientId(Long patientId) {
            record.setPatientId(patientId);
            return this;
        }

        public Builder message(String message) {
            record.setMessage(message);
            return this;
        }

        public Builder severity(String severity) {
            record.setSeverity(severity);
            return this;
        }

        public Builder alertType(String alertType) {
            record.setAlertType(alertType);
            return this;
        }

        public Builder resolved(boolean resolved) {
            record.setResolved(resolved);
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            record.setCreatedAt(createdAt);
            return this;
        }

        public ClinicalAlertRecord build() {
            return record;
        }
    }

    /* ========= GETTERS / SETTERS ========= */

    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getMessage() { return message; }
    public String getSeverity() { return severity; }
    public String getAlertType() { return alertType; }
    public boolean isResolved() { return resolved; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setMessage(String message) { this.message = message; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
