package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_alert_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicalAlertRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    
    @Column(name = "log_id")
    private Long logId;
    
    @Column(name = "rule_id")
    private Long ruleId;
    
    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType; // PAIN_SPIKE, MOBILITY_DROP, FATIGUE_SPIKE, WELLNESS_DEVIATION
    
    @Column(name = "severity", nullable = false, length = 20)
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "parameter", nullable = false, length = 20)
    private String parameter; // PAIN, MOBILITY, FATIGUE, WELLNESS_SCORE
    
    @Column(nullable = false)
    private Integer actualValue;
    
    @Column(name = "expected_value")
    private Integer expectedValue;
    
    @Column(name = "deviation_amount")
    private Integer deviationAmount;
    
    @Column(length = 1000)
    private String message;
    
    @Column(name = "is_resolved")
    @Builder.Default
    private Boolean resolved = false;
    
    @Column(name = "resolved_by")
    private Long resolvedBy;
    
    @Column(name = "resolution_notes", length = 1000)
    private String resolutionNotes;
    
    @Column(name = "acknowledged")
    @Builder.Default
    private Boolean acknowledged = false;
    
    @Column(name = "acknowledged_by")
    private Long acknowledgedBy;
    
    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(name = "notification_sent")
    @Builder.Default
    private Boolean notificationSent = false;
    
    @Column(name = "notification_channels", length = 100)
    private String notificationChannels;
    
    @Column(name = "escalation_level")
    @Builder.Default
    private Integer escalationLevel = 1;
    
    // Helper methods
    public void acknowledge(Long userId) {
        this.acknowledged = true;
        this.acknowledgedBy = userId;
        this.acknowledgedAt = LocalDateTime.now();
    }
    
    public void resolve(Long userId, String notes) {
        this.resolved = true;
        this.resolvedBy = userId;
        this.resolvedAt = LocalDateTime.now();
        this.resolutionNotes = notes;
    }
    
    public boolean isCritical() {
        return "CRITICAL".equals(severity);
    }
    
    public boolean requiresImmediateAttention() {
        return isCritical() || (!acknowledged && "HIGH".equals(severity));
    }
    
    public Long getAgeInHours() {
        if (createdAt == null) return 0L;
        return java.time.temporal.ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
    }
}