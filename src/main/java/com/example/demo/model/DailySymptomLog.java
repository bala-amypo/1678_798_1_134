package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailySymptomLog {

    private Long id;
    private Long patientId;
    private LocalDate logDate;
    private Integer painLevel;
    private Integer mobilityLevel;
    private Integer fatigueLevel;
    private String additionalNotes;
}
