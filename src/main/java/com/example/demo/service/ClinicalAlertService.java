package com.example.demo.service;

import com.example.demo.model.ClinicalAlertRecord;

import java.util.List;
import java.util.Optional;

public interface ClinicalAlertService {
    ClinicalAlertRecord createAlert(ClinicalAlertRecord alert);
    ClinicalAlertRecord resolveAlert(Long id);
    List<ClinicalAlertRecord> getAlertsByPatient(Long patientId);
    List<ClinicalAlertRecord> getAllAlerts();
    Optional<ClinicalAlertRecord> getAlertById(Long id);
}
