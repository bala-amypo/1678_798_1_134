package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_symptom_logs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"patientId", "logDate"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySymptomLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long patientId;
    
    @Column(nullable = false)
    private LocalDate logDate;
    
    private Integer painLevel; // 0-10 scale
    private Integer mobilityLevel; // 0-10 scale
    private Integer fatigueLevel; // 0-10 scale
    
    private String additionalNotes;
}