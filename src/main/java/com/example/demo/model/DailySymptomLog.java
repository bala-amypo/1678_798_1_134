package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_symptom_logs", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"patient_id", "log_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailySymptomLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    
    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;
    
    @Column(name = "pain_level", nullable = false)
    @Builder.Default
    private Integer painLevel = 0; // 0-10 scale
    
    @Column(name = "mobility_level", nullable = false)
    @Builder.Default
    private Integer mobilityLevel = 0; // 0-10 scale
    
    @Column(name = "fatigue_level", nullable = false)
    @Builder.Default
    private Integer fatigueLevel = 0; // 0-10 scale
    
    @Column(name = "medication_taken")
    @Builder.Default
    private Boolean medicationTaken = false;
    
    @Column(name = "medication_details", length = 500)
    private String medicationDetails;
    
    @Column(name = "therapy_completed")
    @Builder.Default
    private Boolean therapyCompleted = false;
    
    @Column(name = "therapy_details", length = 500)
    private String therapyDetails;
    
    @Column(name = "additional_notes", length = 1000)
    private String additionalNotes;
    
    @Column(name = "recorded_by")
    private Long recordedBy; // User ID of the clinician/assistant
    
    @Column(name = "recorded_at")
    @Builder.Default
    private LocalDate recordedAt = LocalDate.now();
    
    // Helper method to calculate overall wellness score
    public Double getWellnessScore() {
        // Reverse pain and fatigue for positive score (lower is better)
        double normalizedPain = (10 - painLevel) / 10.0;
        double normalizedMobility = mobilityLevel / 10.0;
        double normalizedFatigue = (10 - fatigueLevel) / 10.0;
        
        // Weighted average (pain 40%, mobility 40%, fatigue 20%)
        return (normalizedPain * 0.4) + (normalizedMobility * 0.4) + (normalizedFatigue * 0.2);
    }
    
    // Helper method to check if log is for today
    public boolean isToday() {
        return logDate.equals(LocalDate.now());
    }
}