package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_alert_records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalAlertRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long patientId;
    
    private Long logId;
    
    @Column(nullable = false)
    private String alertType;
    
    @Column(nullable = false)
    private String severity;
    
    private String message;
    
    @Builder.Default
    private Boolean resolved = false;
    
    @CreatedDate
    private LocalDateTime createdAt;
}