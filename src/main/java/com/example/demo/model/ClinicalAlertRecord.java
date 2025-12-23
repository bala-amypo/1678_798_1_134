package com.example.demo.model;

public class ClinicalAlertRecord {

    private Long id;
    private Long patientId;
    private Long logId;
    private String alertType;
    private String message;
    private boolean resolved = false;

    public ClinicalAlertRecord() {
    }

    // ---------- BUILDER ----------
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ClinicalAlertRecord record = new ClinicalAlertRecord();

        public Builder id(Long id) {
            record.setId(id);
            return this;
        }

        public Builder patientId(Long patientId) {
            record.setPatientId(patientId);
            return this;
        }

        public Builder logId(Long logId) {
            record.setLogId(logId);
            return this;
        }

        public Builder alertType(String alertType) {
            record.setAlertType(alertType);
            return this;
        }

        public Builder message(String message) {   // ✅ FIX
            record.setMessage(message);
            return this;
        }

        public Builder resolved(boolean resolved) {
            record.setResolved(resolved);
            return this;
        }

        public ClinicalAlertRecord build() {
            return record;
        }
    }

    // ---------- GETTERS / SETTERS ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean getResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
}
