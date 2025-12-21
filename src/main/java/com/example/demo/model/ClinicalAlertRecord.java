package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Builder.Default
    private Boolean resolved = false;

    @CreationTimestamp
    private LocalDate alertDate;

    public ClinicalAlertRecord(Long patientId, Long logId, String alertType, String severity, String message) {
        this.patientId = patientId;
        this.logId = logId;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.resolved = false;
    }

    // Manual getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
    
    public LocalDate getAlertDate() { return alertDate; }
    public void setAlertDate(LocalDate alertDate) { this.alertDate = alertDate; }

    // Static builder method
    public static ClinicalAlertRecordBuilder builder() {
        return new ClinicalAlertRecordBuilder();
    }

    public static class ClinicalAlertRecordBuilder {
        private Long patientId;
        private Long logId;
        private String alertType;
        private String severity;
        private String message;
        
        public ClinicalAlertRecordBuilder patientId(Long patientId) {
            this.patientId = patientId;
            return this;
        }
        
        public ClinicalAlertRecordBuilder logId(Long logId) {
            this.logId = logId;
            return this;
        }
        
        public ClinicalAlertRecordBuilder alertType(String alertType) {
            this.alertType = alertType;
            return this;
        }
        
        public ClinicalAlertRecordBuilder severity(String severity) {
            this.severity = severity;
            return this;
        }
        
        public ClinicalAlertRecordBuilder message(String message) {
            this.message = message;
            return this;
        }
        
        public ClinicalAlertRecord build() {
            return new ClinicalAlertRecord(patientId, logId, alertType, severity, message);
        }
    }
}