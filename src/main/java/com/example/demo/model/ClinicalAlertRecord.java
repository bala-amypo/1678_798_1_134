package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClinicalAlertRecord {

    private Long id;
    private Long patientId;
    private Long logId;
    private String alertType;
    private String severity;
    private String message;
    private Boolean resolved = false;
}
