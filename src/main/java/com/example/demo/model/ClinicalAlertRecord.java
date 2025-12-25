package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class ClinicalAlertRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long patientId;
    private Long logId;
    private String alertType;
    private String severity;
    private String message;
    private Boolean resolved;

    public ClinicalAlertRecord() {
    }

    public ClinicalAlertRecord(Long id, Long patientId, Long logId, String alertType, String severity, String message, Boolean resolved) {
        this.id = id;
        this.patientId = patientId;
        this.logId = logId;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.resolved = resolved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicalAlertRecord that = (ClinicalAlertRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(patientId, that.patientId) && Objects.equals(logId, that.logId) && Objects.equals(alertType, that.alertType) && Objects.equals(severity, that.severity) && Objects.equals(message, that.message) && Objects.equals(resolved, that.resolved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, logId, alertType, severity, message, resolved);
    }

    @Override
    public String toString() {
        return "ClinicalAlertRecord{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", logId=" + logId +
                ", alertType='" + alertType + '\'' +
                ", severity='" + severity + '\'' +
                ", message='" + message + '\'' +
                ", resolved=" + resolved +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long patientId;
        private Long logId;
        private String alertType;
        private String severity;
        private String message;
        private Boolean resolved;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder patientId(Long patientId) {
            this.patientId = patientId;
            return this;
        }

        public Builder logId(Long logId) {
            this.logId = logId;
            return this;
        }

        public Builder alertType(String alertType) {
            this.alertType = alertType;
            return this;
        }

        public Builder severity(String severity) {
            this.severity = severity;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder resolved(Boolean resolved) {
            this.resolved = resolved;
            return this;
        }

        public ClinicalAlertRecord build() {
            return new ClinicalAlertRecord(id, patientId, logId, alertType, severity, message, resolved);
        }
    }
}