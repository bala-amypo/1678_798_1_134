package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_symptom_logs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"patientId", "logDate"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySymptomLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long patientId;

    @NotNull
    @PastOrPresent
    private LocalDate logDate;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer painLevel;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer mobilityLevel;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer fatigueLevel;

    @Lob
    @Size(max = 2000)
    private String additionalNotes;

    @CreationTimestamp
    private LocalDateTime submittedAt;

    public DailySymptomLog(Long patientId, LocalDate logDate, Integer painLevel, Integer mobilityLevel, Integer fatigueLevel) {
        this.patientId = patientId;
        this.logDate = logDate;
        this.painLevel = painLevel;
        this.mobilityLevel = mobilityLevel;
        this.fatigueLevel = fatigueLevel;
    }

    // Manual getters and setters
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
    
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
private Integer painLevel;
private Integer fatigueLevel;
private LocalDate logDate;
