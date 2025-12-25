package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.repository.ClinicalAlertRecordRepository;
import com.example.demo.service.ClinicalAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClinicalAlertServiceImpl implements ClinicalAlertService {
    
    private final ClinicalAlertRecordRepository clinicalAlertRecordRepository;
    
    @Override
    public ClinicalAlertRecord createAlert(ClinicalAlertRecord alert) {
        log.info("Creating clinical alert for patient ID: {}", alert.getPatientId());
        
        // Set default values
        if (alert.getCreatedAt() == null) {
            alert.setCreatedAt(LocalDateTime.now());
        }
        if (alert.getResolved() == null) {
            alert.setResolved(false);
        }
        if (alert.getAcknowledged() == null) {
            alert.setAcknowledged(false);
        }
        if (alert.getNotificationSent() == null) {
            alert.setNotificationSent(false);
        }
        if (alert.getEscalationLevel() == null) {
            alert.setEscalationLevel(1);
        }
        
        ClinicalAlertRecord savedAlert = clinicalAlertRecordRepository.save(alert);
        log.info("Clinical alert created with ID: {}", savedAlert.getId());
        
        return savedAlert;
    }
    
    @Override
    public List<ClinicalAlertRecord> getAllAlerts() {
        log.debug("Fetching all clinical alerts");
        return clinicalAlertRecordRepository.findAll();
    }
    
    @Override
    public List<ClinicalAlertRecord> getUnresolvedAlerts() {
        log.debug("Fetching unresolved clinical alerts");
        return clinicalAlertRecordRepository.findByResolvedFalse();
    }
    
    @Override
    public List<ClinicalAlertRecord> getAlertsByPatient(Long patientId) {
        log.debug("Fetching clinical alerts for patient ID: {}", patientId);
        return clinicalAlertRecordRepository.findByPatientId(patientId);
    }
    
    @Override
    public List<ClinicalAlertRecord> getUnresolvedAlertsByPatient(Long patientId) {
        log.debug("Fetching unresolved alerts for patient ID: {}", patientId);
        return clinicalAlertRecordRepository.findByPatientIdAndResolvedFalse(patientId);
    }
    
    @Override
    public List<ClinicalAlertRecord> getHighPriorityAlerts() {
        log.debug("Fetching high priority clinical alerts");
        return clinicalAlertRecordRepository.findHighPriorityUnresolvedAlerts();
    }
    
    @Override
    public Optional<ClinicalAlertRecord> getAlertById(Long id) {
        log.debug("Fetching clinical alert by ID: {}", id);
        return clinicalAlertRecordRepository.findById(id);
    }
    
    @Override
    public ClinicalAlertRecord acknowledgeAlert(Long alertId, Long userId) {
        log.info("Acknowledging clinical alert ID: {}", alertId);
        
        ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical alert", "id", alertId));
        
        if (alert.getResolved()) {
            throw new IllegalArgumentException("Cannot acknowledge a resolved alert");
        }
        
        alert.acknowledge(userId);
        ClinicalAlertRecord updatedAlert = clinicalAlertRecordRepository.save(alert);
        
        log.info("Clinical alert acknowledged by user ID: {}", userId);
        return updatedAlert;
    }
    
    @Override
    public ClinicalAlertRecord resolveAlert(Long alertId, Long userId, String notes) {
        log.info("Resolving clinical alert ID: {}", alertId);
        
        ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical alert", "id", alertId));
        
        if (alert.getResolved()) {
            throw new IllegalArgumentException("Alert is already resolved");
        }
        
        alert.resolve(userId, notes);
        ClinicalAlertRecord resolvedAlert = clinicalAlertRecordRepository.save(alert);
        
        log.info("Clinical alert resolved by user ID: {}", userId);
        return resolvedAlert;
    }
    
    @Override
    public void deleteAlert(Long id) {
        log.info("Deleting clinical alert ID: {}", id);
        
        ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical alert", "id", id));
        
        clinicalAlertRecordRepository.delete(alert);
        log.info("Clinical alert deleted successfully");
    }
    
    @Override
    public Long countUnresolvedAlerts() {
        log.debug("Counting unresolved clinical alerts");
        return clinicalAlertRecordRepository.findByResolvedFalse().stream().count();
    }
    
    @Override
    public List<ClinicalAlertRecord> getAlertsRequiringImmediateAttention() {
        log.debug("Fetching alerts requiring immediate attention");
        return clinicalAlertRecordRepository.findAlertsRequiringImmediateAttention();
    }
    
    @Override
    public Map<String, Long> getAlertStatistics() {
        log.debug("Generating alert statistics");
        
        Map<String, Long> statistics = new HashMap<>();
        
        // Total alerts
        long totalAlerts = clinicalAlertRecordRepository.count();
        statistics.put("totalAlerts", totalAlerts);
        
        // Unresolved alerts
        long unresolvedAlerts = countUnresolvedAlerts();
        statistics.put("unresolvedAlerts", unresolvedAlerts);
        
        // High priority unresolved alerts
        List<ClinicalAlertRecord> highPriority = getHighPriorityAlerts();
        statistics.put("highPriorityAlerts", (long) highPriority.size());
        
        // Unacknowledged alerts
        List<ClinicalAlertRecord> unacknowledged = clinicalAlertRecordRepository
                .findByResolvedFalseAndAcknowledgedFalse();
        statistics.put("unacknowledgedAlerts", (long) unacknowledged.size());
        
        // Alerts by severity (last 30 days)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Object[]> severityCounts = clinicalAlertRecordRepository
                .countAlertsBySeveritySince(thirtyDaysAgo);
        
        for (Object[] count : severityCounts) {
            String severity = (String) count[0];
            Long countValue = (Long) count[1];
            statistics.put(severity + "AlertsLast30Days", countValue);
        }
        
        return statistics;
    }
    
    @Override
    public ClinicalAlertRecord escalateAlert(Long alertId) {
        log.info("Escalating clinical alert ID: {}", alertId);
        
        ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical alert", "id", alertId));
        
        if (alert.getResolved()) {
            throw new IllegalArgumentException("Cannot escalate a resolved alert");
        }
        
        // Increase escalation level
        int currentLevel = alert.getEscalationLevel() != null ? alert.getEscalationLevel() : 1;
        alert.setEscalationLevel(currentLevel + 1);
        
        // Update severity if needed
        if (currentLevel >= 2 && !alert.isCritical()) {
            alert.setSeverity("HIGH");
        }
        if (currentLevel >= 3 && !alert.isCritical()) {
            alert.setSeverity("CRITICAL");
        }
        
        alert.setMessage(alert.getMessage() + " [ESCALATED to level " + alert.getEscalationLevel() + "]");
        
        ClinicalAlertRecord escalatedAlert = clinicalAlertRecordRepository.save(alert);
        log.info("Alert escalated to level {}", escalatedAlert.getEscalationLevel());
        
        return escalatedAlert;
    }
    
    @Override
    public List<ClinicalAlertRecord> getAlertsBySeverity(String severity) {
        log.debug("Fetching clinical alerts with severity: {}", severity);
        return clinicalAlertRecordRepository.findBySeverity(severity);
    }
    
    // Helper method to mark notification as sent
    public ClinicalAlertRecord markNotificationSent(Long alertId) {
        log.debug("Marking notification as sent for alert ID: {}", alertId);
        
        ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical alert", "id", alertId));
        
        alert.setNotificationSent(true);
        return clinicalAlertRecordRepository.save(alert);
    }
    
    // Helper method to get alerts for multiple patients
    public List<ClinicalAlertRecord> getAlertsForPatients(List<Long> patientIds) {
        log.debug("Fetching alerts for {} patients", patientIds.size());
        return clinicalAlertRecordRepository.findUnresolvedAlertsForPatients(patientIds);
    }
    
    // Helper method to get alert aging report
    public Map<String, Object> getAlertAgingReport() {
        log.debug("Generating alert aging report");
        
        Map<String, Object> report = new HashMap<>();
        List<ClinicalAlertRecord> unresolvedAlerts = getUnresolvedAlerts();
        
        // Categorize alerts by age
        List<ClinicalAlertRecord> lessThan24Hours = new ArrayList<>();
        List<ClinicalAlertRecord> oneToThreeDays = new ArrayList<>();
        List<ClinicalAlertRecord> moreThanThreeDays = new ArrayList<>();
        
        LocalDateTime now = LocalDateTime.now();
        
        for (ClinicalAlertRecord alert : unresolvedAlerts) {
            long ageInHours = alert.getAgeInHours();
            
            if (ageInHours < 24) {
                lessThan24Hours.add(alert);
            } else if (ageInHours >= 24 && ageInHours <= 72) {
                oneToThreeDays.add(alert);
            } else {
                moreThanThreeDays.add(alert);
            }
        }
        
        report.put("lessThan24Hours", lessThan24Hours);
        report.put("oneToThreeDays", oneToThreeDays);
        report.put("moreThanThreeDays", moreThanThreeDays);
        report.put("totalUnresolved", unresolvedAlerts.size());
        
        return report;
    }
    
    // Helper method to batch acknowledge alerts
    public int batchAcknowledgeAlerts(List<Long> alertIds, Long userId) {
        log.info("Batch acknowledging {} alerts by user ID: {}", alertIds.size(), userId);
        
        int acknowledgedCount = 0;
        
        for (Long alertId : alertIds) {
            try {
                ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(alertId)
                        .orElseThrow(() -> new ResourceNotFoundException("Clinical alert", "id", alertId));
                
                if (!alert.getResolved() && !alert.getAcknowledged()) {
                    alert.acknowledge(userId);
                    clinicalAlertRecordRepository.save(alert);
                    acknowledgedCount++;
                }
            } catch (Exception e) {
                log.error("Failed to acknowledge alert ID {}: {}", alertId, e.getMessage());
            }
        }
        
        log.info("Batch acknowledged {} alerts", acknowledgedCount);
        return acknowledgedCount;
    }
    
    // Helper method to batch resolve alerts
    public int batchResolveAlerts(List<Long> alertIds, Long userId, String notes) {
        log.info("Batch resolving {} alerts by user ID: {}", alertIds.size(), userId);
        
        int resolvedCount = 0;
        
        for (Long alertId : alertIds) {
            try {
                ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(alertId)
                        .orElseThrow(() -> new ResourceNotFoundException("Clinical alert", "id", alertId));
                
                if (!alert.getResolved()) {
                    alert.resolve(userId, notes);
                    clinicalAlertRecordRepository.save(alert);
                    resolvedCount++;
                }
            } catch (Exception e) {
                log.error("Failed to resolve alert ID {}: {}", alertId, e.getMessage());
            }
        }
        
        log.info("Batch resolved {} alerts", resolvedCount);
        return resolvedCount;
    }
}