package com.example.demo.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "recovery_curve_profiles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"surgeryType", "dayNumber"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryCurveProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String surgeryType;
    
    @Column(nullable = false)
    private Integer dayNumber;
    
    private Integer expectedPainLevel;
    private Integer expectedMobilityLevel;
    private Integer expectedFatigueLevel;
}