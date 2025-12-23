package com.example.demo.model;

import java.time.LocalDate;

public class DailySymptomLog {

    private Long id;
    private Long patientId;
    private LocalDate logDate;
    private Integer painLevel;
    private Integer mobilityLevel;
    private Integer fatigueLevel;
    private String additionalNotes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public Integer getPainLevel() { return painLevel; }
    public void setPainLevel(Integer painLevel) { this.painLevel = painLevel; }

    public Integer getMobilityLevel() { return mobilityLevel; }
    public void setMobilityLevel(Integer mobilityLevel) { this.mobilityLevel = mobilityLevel; }

    public Integer getFatigueLevel() { return fatigueLevel; }
    public void setFatigueLevel(Integer fatigueLevel) { this.fatigueLevel = fatigueLevel; }

    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }
}
