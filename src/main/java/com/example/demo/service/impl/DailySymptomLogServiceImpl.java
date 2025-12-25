package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DailySymptomLogServiceImpl implements DailySymptomLogService {
    
    private final DailySymptomLogRepository dailySymptomLogRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final RecoveryCurveService recoveryCurveService;
    private final DeviationRuleService deviationRuleService;
    private final ClinicalAlertService clinicalAlertService;
    
    @Override
    public DailySymptomLog recordSymptomLog(DailySymptomLog log) {
        log.info("Recording symptom log for patient ID: {}", log.getPatientId());
        
        // Validate patient exists and is active
        PatientProfile patient = patientProfileRepository.findById(log.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", log.getPatientId()));
        
        if (!patient.getActive()) {
            throw new IllegalArgumentException("Cannot record symptoms for inactive patient");
        }
        
        // Check for duplicate log for same date
        if (hasLogForDate(log.getPatientId(), log.getLogDate())) {
            throw new IllegalArgumentException("Symptom log already exists for this date");
        }
        
        // Set default date if not provided
        if (log.getLogDate() == null) {
            log.setLogDate(LocalDate.now());
        }
        
        // Set recorded date
        log.setRecordedAt(LocalDate.now());
        
        // Save the log
        DailySymptomLog savedLog = dailySymptomLogRepository.save(log);
        
        // Check for deviations and generate alerts
        checkForDeviations(savedLog, patient);
        
        log.info("Symptom log recorded successfully. Log ID: {}", savedLog.getId());
        
        return savedLog;
    }
    
    @Override
    public DailySymptomLog updateSymptomLog(Long logId, DailySymptomLog updates) {
        log.info("Updating symptom log with ID: {}", logId);
        
        DailySymptomLog existingLog = dailySymptomLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Symptom log", "id", logId));
        
        // Update fields if provided
        if (updates.getPainLevel() != null) {
            existingLog.setPainLevel(updates.getPainLevel());
        }
        if (updates.getMobilityLevel() != null) {
            existingLog.setMobilityLevel(updates.getMobilityLevel());
        }
        if (updates.getFatigueLevel() != null) {
            existingLog.setFatigueLevel(updates.getFatigueLevel());
        }
        if (updates.getMedicationTaken() != null) {
            existingLog.setMedicationTaken(updates.getMedicationTaken());
        }
        if (updates.getMedicationDetails() != null) {
            existingLog.setMedicationDetails(updates.getMedicationDetails());
        }
        if (updates.getTherapyCompleted() != null) {
            existingLog.setTherapyCompleted(updates.getTherapyCompleted());
        }
        if (updates.getTherapyDetails() != null) {
            existingLog.setTherapyDetails(updates.getTherapyDetails());
        }
        if (updates.getAdditionalNotes() != null) {
            existingLog.setAdditionalNotes(updates.getAdditionalNotes());
        }
        
        DailySymptomLog updatedLog = dailySymptomLogRepository.save(existingLog);
        
        // Re-check for deviations after update
        PatientProfile patient = patientProfileRepository.findById(updatedLog.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", updatedLog.getPatientId()));
        checkForDeviations(updatedLog, patient);
        
        log.info("Symptom log updated successfully");
        
        return updatedLog;
    }
    
    @Override
    public Optional<DailySymptomLog> getLogById(Long logId) {
        log.debug("Fetching symptom log by ID: {}", logId);
        return dailySymptomLogRepository.findById(logId);
    }
    
    @Override
    public List<DailySymptomLog> getLogsByPatient(Long patientId) {
        log.debug("Fetching symptom logs for patient ID: {}", patientId);
        return dailySymptomLogRepository.findByPatientId(patientId);
    }
    
    @Override
    public List<DailySymptomLog> getLogsByPatientAndDateRange(Long patientId, LocalDate start, LocalDate end) {
        log.debug("Fetching symptom logs for patient {} from {} to {}", patientId, start, end);
        return dailySymptomLogRepository.findByPatientIdAndLogDateBetween(patientId, start, end);
    }
    
    @Override
    public List<DailySymptomLog> getRecentLogs(Long patientId, int limit) {
        log.debug("Fetching recent {} logs for patient {}", limit, patientId);
        // Implementation would use pagination
        List<DailySymptomLog> allLogs = dailySymptomLogRepository.findByPatientId(patientId);
        allLogs.sort((a, b) -> b.getLogDate().compareTo(a.getLogDate()));
        
        return allLogs.stream()
                .limit(limit)
                .toList();
    }
    
    @Override
    public void deleteLog(Long logId) {
        log.info("Deleting symptom log with ID: {}", logId);
        
        DailySymptomLog log = dailySymptomLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Symptom log", "id", logId));
        
        dailySymptomLogRepository.delete(log);
        log.info("Symptom log deleted successfully");
    }
    
    @Override
    public boolean hasLogForDate(Long patientId, LocalDate date) {
        return dailySymptomLogRepository.existsByPatientIdAndLogDate(patientId, date);
    }
    
    @Override
    public Map<String, Double> calculatePatientAverages(Long patientId) {
        log.debug("Calculating averages for patient ID: {}", patientId);
        
        List<DailySymptomLog> logs = dailySymptomLogRepository.findByPatientId(patientId);
        
        if (logs.isEmpty()) {
            return Map.of(
                "pain", 0.0,
                "mobility", 0.0,
                "fatigue", 0.0,
                "wellness", 0.0
            );
        }
        
        double painSum = 0;
        double mobilitySum = 0;
        double fatigueSum = 0;
        double wellnessSum = 0;
        
        for (DailySymptomLog log : logs) {
            painSum += log.getPainLevel();
            mobilitySum += log.getMobilityLevel();
            fatigueSum += log.getFatigueLevel();
            wellnessSum += log.getWellnessScore();
        }
        
        int count = logs.size();
        
        return Map.of(
            "pain", painSum / count,
            "mobility", mobilitySum / count,
            "fatigue", fatigueSum / count,
            "wellness", wellnessSum / count
        );
    }
    
    @Override
    public List<DailySymptomLog> getLogsWithHighPain(Long patientId, Integer threshold) {
        log.debug("Fetching logs with pain above {} for patient {}", threshold, patientId);
        LocalDate startDate = LocalDate.now().minusMonths(1);
        return dailySymptomLogRepository.findHighPainLogs(threshold, startDate).stream()
                .filter(log -> log.getPatientId().equals(patientId))
                .toList();
    }
    
    @Override
    public List<LocalDate> getMissingLogDates(Long patientId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding missing log dates for patient {} from {} to {}", patientId, startDate, endDate);
        return dailySymptomLogRepository.findMissingLogDates(patientId, startDate, endDate);
    }
    
    @Override
    public DailySymptomLog getOrCreateTodayLog(Long patientId, Long recordedBy) {
        LocalDate today = LocalDate.now();
        
        Optional<DailySymptomLog> existingLog = dailySymptomLogRepository
                .findByPatientIdAndLogDate(patientId, today);
        
        if (existingLog.isPresent()) {
            return existingLog.get();
        }
        
        // Create new log for today
        DailySymptomLog newLog = DailySymptomLog.builder()
                .patientId(patientId)
                .logDate(today)
                .painLevel(0)
                .mobilityLevel(0)
                .fatigueLevel(0)
                .recordedBy(recordedBy)
                .recordedAt(today)
                .build();
        
        return recordSymptomLog(newLog);
    }
    
    private void checkForDeviations(DailySymptomLog log, PatientProfile patient) {
        log.debug("Checking for deviations for log ID: {}", log.getId());
        
        // Calculate days since surgery
        long daysSinceSurgery = java.time.temporal.ChronoUnit.DAYS.between(
                patient.getSurgeryDate().toLocalDate(), 
                log.getLogDate()
        );
        
        if (daysSinceSurgery < 0) {
            log.warn("Log date is before surgery date for patient {}", patient.getId());
            return;
        }
        
        // Get recovery curve for patient's surgery type
        Optional<RecoveryCurveProfile> expectedCurve = recoveryCurveService
                .getCurveEntry(patient.getSurgeryType(), (int) daysSinceSurgery);
        
        if (expectedCurve.isEmpty()) {
            log.debug("No recovery curve found for day {}", daysSinceSurgery);
            return;
        }
        
        RecoveryCurveProfile expected = expectedCurve.get();
        
        // Get active deviation rules
        List<DeviationRule> rules = deviationRuleService.getActiveRules();
        
        // Check each rule
        for (DeviationRule rule : rules) {
            boolean isViolated = false;
            Integer actualValue = null;
            Integer expectedValue = null;
            
            switch (rule.getParameter()) {
                case "PAIN":
                    actualValue = log.getPainLevel();
                    expectedValue = expected.getExpectedPainLevel();
                    isViolated = deviationRuleService.checkRuleViolation(rule, actualValue, expectedValue);
                    break;
                    
                case "MOBILITY":
                    actualValue = log.getMobilityLevel();
                    expectedValue = expected.getExpectedMobilityLevel();
                    isViolated = deviationRuleService.checkRuleViolation(rule, actualValue, expectedValue);
                    break;
                    
                case "FATIGUE":
                    actualValue = log.getFatigueLevel();
                    expectedValue = expected.getExpectedFatigueLevel();
                    isViolated = deviationRuleService.checkRuleViolation(rule, actualValue, expectedValue);
                    break;
                    
                case "WELLNESS_SCORE":
                    double actualWellness = log.getWellnessScore();
                    double expectedWellness = expected.getExpectedWellnessScore();
                    int wellnessDiff = (int) Math.abs((actualWellness - expectedWellness) * 10);
                    isViolated = rule.evaluate(wellnessDiff);
                    actualValue = (int) (actualWellness * 10);
                    expectedValue = (int) (expectedWellness * 10);
                    break;
            }
            
            if (isViolated && actualValue != null) {
                createClinicalAlert(log, patient, rule, actualValue, expectedValue);
            }
        }
    }
    
    private void createClinicalAlert(DailySymptomLog log, PatientProfile patient, 
                                     DeviationRule rule, Integer actualValue, Integer expectedValue) {
        ClinicalAlertRecord alert = ClinicalAlertRecord.builder()
                .patientId(log.getPatientId())
                .logId(log.getId())
                .ruleId(rule.getId())
                .alertType(rule.getParameter() + "_DEVIATION")
                .severity(rule.getSeverity())
                .parameter(rule.getParameter())
                .actualValue(actualValue)
                .expectedValue(expectedValue)
                .deviationAmount(Math.abs(actualValue - expectedValue))
                .message(rule.generateAlertMessage(actualValue, expectedValue))
                .resolved(false)
                .notificationChannels(rule.getNotificationChannels())
                .escalationLevel(1)
                .build();
        
        clinicalAlertService.createAlert(alert);
        log.info("Clinical alert created for patient {}: {}", patient.getPatientId(), alert.getMessage());
    }
}