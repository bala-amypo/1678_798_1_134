package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoveryCurveProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String surgeryType;
    private Integer dayNumber;
    private Integer expectedPainLevel;
    private Integer expectedMobilityLevel;
    private Integer expectedFatigueLevel;
}
