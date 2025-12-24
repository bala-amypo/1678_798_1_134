package com.example.demo.service;

import com.example.demo.model.ClinicalAlertRecord;

import java.util.List;
import java.util.Optional;

public interface ClinicalAlertService {

    List<ClinicalAlertRecord> getAllAlerts();

    Optional<ClinicalAlertRecord> getAlertById(Long id);

    List<ClinicalAlertRecord> getAlertsByPatient(Long patientId);

    ClinicalAlertRecord resolveAlert(Long id);
}
