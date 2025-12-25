package com.example.demo.service;

import com.example.demo.model.ClinicalAlertRecord;
import java.util.List;
import java.util.Optional;

public interface ClinicalAlertService {
    ClinicalAlertRecord createAlert(ClinicalAlertRecord alert);
    List<ClinicalAlertRecord> getAllAlerts();
    List<ClinicalAlertRecord> getUnresolvedAlerts();
    List<ClinicalAlertRecord> getAlertsByPatient(Long patientId);
    List<ClinicalAlertRecord> getUnresolvedAlertsByPatient(Long patientId);
    List<ClinicalAlertRecord> getHighPriorityAlerts();
    Optional<ClinicalAlertRecord> getAlertById(Long id);
    ClinicalAlertRecord acknowledgeAlert(Long alertId, Long userId);
    ClinicalAlertRecord resolveAlert(Long alertId, Long userId, String notes);
    void deleteAlert(Long id);
    Long countUnresolvedAlerts();
    List<ClinicalAlertRecord> getAlertsRequiringImmediateAttention();
    Map<String, Long> getAlertStatistics();
    ClinicalAlertRecord escalateAlert(Long alertId);
    List<ClinicalAlertRecord> getAlertsBySeverity(String severity);
}