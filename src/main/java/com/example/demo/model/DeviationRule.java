package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deviation_rules", 
       uniqueConstraints = @UniqueConstraint(columnNames = "rule_code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviationRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rule_code", nullable = false, unique = true, length = 50)
    private String ruleCode;
    
    @Column(name = "parameter", nullable = false, length = 20)
    private String parameter; // PAIN, MOBILITY, FATIGUE, WELLNESS_SCORE
    
    @Column(nullable = false)
    private Integer threshold;
    
    @Column(name = "operator", nullable = false, length = 20)
    @Builder.Default
    private String operator = "GREATER_THAN"; // GREATER_THAN, LESS_THAN, EQUALS, BETWEEN
    
    @Column(name = "max_threshold")
    private Integer maxThreshold; // For BETWEEN operator
    
    @Column(name = "severity", nullable = false, length = 20)
    @Builder.Default
    private String severity = "MEDIUM"; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "alert_message_template", length = 500)
    private String alertMessageTemplate;
    
    @Column(length = 1000)
    private String description;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;
    
    @Column(name = "notification_channels", length = 100)
    @Builder.Default
    private String notificationChannels = "IN_APP"; // IN_APP, EMAIL, SMS, ALL
    
    @Column(name = "action_required")
    @Builder.Default
    private Boolean actionRequired = false;
    
    @Column(name = "action_instructions", length = 1000)
    private String actionInstructions;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper method to evaluate rule
    public boolean evaluate(Integer value) {
        switch (operator) {
            case "GREATER_THAN":
                return value > threshold;
            case "LESS_THAN":
                return value < threshold;
            case "EQUALS":
                return value.equals(threshold);
            case "BETWEEN":
                return maxThreshold != null && value >= threshold && value <= maxThreshold;
            default:
                return false;
        }
    }
    
    // Helper method to evaluate rule with two values (for ranges)
    public boolean evaluate(Integer actualValue, Integer expectedValue) {
        int deviation = Math.abs(actualValue - expectedValue);
        return evaluate(deviation);
    }
    
    // Helper method to generate alert message
    public String generateAlertMessage(Integer actualValue, Integer expectedValue) {
        if (alertMessageTemplate != null) {
            return alertMessageTemplate
                    .replace("{actual}", String.valueOf(actualValue))
                    .replace("{expected}", String.valueOf(expectedValue))
                    .replace("{parameter}", parameter);
        }
        
        return String.format("%s deviation detected. Actual: %d, Expected: %d. Severity: %s",
                parameter, actualValue, expectedValue, severity);
    }
}