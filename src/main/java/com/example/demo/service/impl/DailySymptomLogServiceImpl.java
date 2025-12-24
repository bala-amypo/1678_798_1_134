package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.ClinicalAlertService;
import com.example.demo.service.DailySymptomLogService;
import com.example.demo.service.DeviationRuleService;
import com.example.demo.service.RecoveryCurveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailySymptomLogServiceImpl implements DailySymptomLogService {
    
    private final DailySymptomLogRepository dailySymptomLogRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final RecoveryCurveService recoveryCurveService;
    private final DeviationRuleService deviationRuleService;
    private final ClinicalAlertService clinicalAlertService;
    
    @Override
    @Transactional
    public DailySymptomLog recordSymptomLog(DailySymptomLog log) {
        // Check patient exists
        PatientProfile patient = patientProfileRepository.findById(log.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + log.getPatientId()));
        
        // Validate patient is active
        if (patient.getActive() != null && !patient.getActive()) {
            throw new IllegalArgumentException("Patient is not active");
        }
        
        // Check for duplicate log for same day
        if (dailySymptomLogRepository.findByPatientIdAndLogDate(log.getPatientId(), log.getLogDate()).isPresent()) {
            throw new IllegalArgumentException("Daily log already exists for this patient and date");
        }
        
        // Validate symptom levels
        validateSymptomLevels(log);
        
        DailySymptomLog savedLog = dailySymptomLogRepository.save(log);
        
        // Check for deviations and create alerts
        checkForDeviationsAndCreateAlerts(savedLog, patient);
        
        return savedLog;
    }
    
    @Override
    public List<DailySymptomLog> getLogsByPatient(Long patientId) {
        patientProfileRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        
        return dailySymptomLogRepository.findByPatientId(patientId);
    }
    
    @Override
    @Transactional
    public DailySymptomLog updateSymptomLog(Long logId, DailySymptomLog updatedLog) {
        DailySymptomLog existingLog = dailySymptomLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Daily log not found with id: " + logId));
        
        // Check patient exists
        patientProfileRepository.findById(updatedLog.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + updatedLog.getPatientId()));
        
        // Validate symptom levels
        validateSymptomLevels(updatedLog);
        
        existingLog.setPainLevel(updatedLog.getPainLevel());
        existingLog.setMobilityLevel(updatedLog.getMobilityLevel());
        existingLog.setFatigueLevel(updatedLog.getFatigueLevel());
        existingLog.setAdditionalNotes(updatedLog.getAdditionalNotes());
        
        DailySymptomLog updated = dailySymptomLogRepository.save(existingLog);
        
        // Re-check for deviations after update
        PatientProfile patient = patientProfileRepository.findById(updatedLog.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        checkForDeviationsAndCreateAlerts(updated, patient);
        
        return updated;
    }
    
    @Override
    @Transactional
    public void deleteSymptomLog(Long id) {
        DailySymptomLog log = dailySymptomLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Daily log not found with id: " + id));
        dailySymptomLogRepository.delete(log);
    }
    
    private void validateSymptomLevels(DailySymptomLog log) {
        if (log.getPainLevel() != null && (log.getPainLevel() < 0 || log.getPainLevel() > 10)) {
            throw new IllegalArgumentException("Pain level must be between 0 and 10");
        }
        if (log.getMobilityLevel() != null && (log.getMobilityLevel() < 0 || log.getMobilityLevel() > 10)) {
            throw new IllegalArgumentException("Mobility level must be between 0 and 10");
        }
        if (log.getFatigueLevel() != null && (log.getFatigueLevel() < 0 || log.getFatigueLevel() > 10)) {
            throw new IllegalArgumentException("Fatigue level must be between 0 and 10");
        }
    }
    
    private void checkForDeviationsAndCreateAlerts(DailySymptomLog log, PatientProfile patient) {
        List<DeviationRule> activeRules = deviationRuleService.getActiveRules();
        
        for (DeviationRule rule : activeRules) {
            Integer symptomLevel = getSymptomLevel(log, rule.getParameter());
            
            if (symptomLevel != null && symptomLevel > rule.getThreshold()) {
                // Check if similar alert already exists for today
                ClinicalAlertRecord alert = ClinicalAlertRecord.builder()
                        .patientId(log.getPatientId())
                        .logId(log.getId())
                        .alertType(rule.getParameter() + "_DEVIATION")
                        .severity(rule.getSeverity())
                        .message(String.format("%s level %d exceeds threshold %d", 
                                rule.getParameter(), symptomLevel, rule.getThreshold()))
                        .resolved(false)
                        .build();
                
                clinicalAlertService.createAlert(alert);
                log.info("Created alert for patient {}: {}", log.getPatientId(), alert.getMessage());
            }
        }
        
        // Check against recovery curve if patient has surgery type and surgery date
        if (patient.getSurgeryType() != null && patient.getSurgeryDate() != null) {
            checkRecoveryCurveDeviations(log, patient);
        }
    }
    
    private void checkRecoveryCurveDeviations(DailySymptomLog log, PatientProfile patient) {
        List<RecoveryCurveProfile> curve = recoveryCurveService.getCurveForSurgery(patient.getSurgeryType());
        if (curve.isEmpty()) {
            return;
        }
        
        // Calculate days since surgery
        long daysSinceSurgery = java.time.temporal.ChronoUnit.DAYS.between(
                patient.getSurgeryDate(), log.getLogDate());
        
        if (daysSinceSurgery >= 0 && daysSinceSurgery < curve.size()) {
            RecoveryCurveProfile expected = curve.get((int) daysSinceSurgery);
            
            checkIndividualDeviation(log.getPainLevel(), expected.getExpectedPainLevel(), 
                    "PAIN", log, "Pain level");
            checkIndividualDeviation(log.getMobilityLevel(), expected.getExpectedMobilityLevel(), 
                    "MOBILITY", log, "Mobility level");
            checkIndividualDeviation(log.getFatigueLevel(), expected.getExpectedFatigueLevel(), 
                    "FATIGUE", log, "Fatigue level");
        }
    }
    
    private void checkIndividualDeviation(Integer actual, Integer expected, 
                                         String parameter, DailySymptomLog log, String label) {
        if (actual != null && expected != null) {
            int deviation = Math.abs(actual - expected);
            if (deviation > 3) { // Significant deviation threshold
                ClinicalAlertRecord alert = ClinicalAlertRecord.builder()
                        .patientId(log.getPatientId())
                        .logId(log.getId())
                        .alertType("RECOVERY_CURVE_DEVIATION")
                        .severity(deviation > 5 ? "HIGH" : "MEDIUM")
                        .message(String.format("%s %d deviates significantly from expected %d", 
                                label, actual, expected))
                        .resolved(false)
                        .build();
                
                clinicalAlertService.createAlert(alert);
            }
        }
    }
    
    private Integer getSymptomLevel(DailySymptomLog log, String parameter) {
        return switch (parameter.toUpperCase()) {
            case "PAIN" -> log.getPainLevel();
            case "MOBILITY" -> log.getMobilityLevel();
            case "FATIGUE" -> log.getFatigueLevel();
            default -> null;
        };
    }
}