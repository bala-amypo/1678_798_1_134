package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.DailySymptomLogRepository;
import com.example.demo.repository.PatientProfileRepository;
import com.example.demo.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DailySymptomLogServiceImpl implements DailySymptomLogService {
    private final DailySymptomLogRepository dailySymptomLogRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final RecoveryCurveService recoveryCurveService;
    private final DeviationRuleService deviationRuleService;
    private final ClinicalAlertService clinicalAlertService;

    public DailySymptomLogServiceImpl(DailySymptomLogRepository dailySymptomLogRepository,
                                    PatientProfileRepository patientProfileRepository,
                                    RecoveryCurveService recoveryCurveService,
                                    DeviationRuleService deviationRuleService,
                                    ClinicalAlertService clinicalAlertService) {
        this.dailySymptomLogRepository = dailySymptomLogRepository;
        this.patientProfileRepository = patientProfileRepository;
        this.recoveryCurveService = recoveryCurveService;
        this.deviationRuleService = deviationRuleService;
        this.clinicalAlertService = clinicalAlertService;
    }

    @Override
    public DailySymptomLog recordSymptomLog(DailySymptomLog log) {
        return createLog(log);
    }

    @Override
    public DailySymptomLog createLog(DailySymptomLog log) {
        // Validate date is not in future
        if (log.getLogDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("future date");
        }
        return dailySymptomLogRepository.save(log);
    }

    private DailySymptomLog recordSymptomLogWithValidation(DailySymptomLog log) {
        // Validate patient exists
        PatientProfile patient = patientProfileRepository.findById(log.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // Validate date is not in future
        if (log.getLogDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("future date");
        }

        // Check for duplicate log
        if (dailySymptomLogRepository.findByPatientIdAndLogDate(log.getPatientId(), log.getLogDate()).isPresent()) {
            throw new IllegalArgumentException("Log already exists for this patient and date");
        }

        DailySymptomLog savedLog = dailySymptomLogRepository.save(log);

        // Check for deviations and create alerts
        checkForDeviations(savedLog, patient);

        return savedLog;
    }

    @Override
    public List<DailySymptomLog> getLogsByPatient(Long patientId) {
        return dailySymptomLogRepository.findByPatientId(patientId);
    }

    @Override
    public List<DailySymptomLog> getLogsByPatientId(Long patientId) {
        return dailySymptomLogRepository.findByPatientId(patientId);
    }

    @Override
    public Optional<DailySymptomLog> getLogById(Long id) {
        return dailySymptomLogRepository.findById(id);
    }

    @Override
    public DailySymptomLog updateSymptomLog(Long id, DailySymptomLog log) {
        DailySymptomLog existing = dailySymptomLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found"));
        
        existing.setPainLevel(log.getPainLevel());
        existing.setMobilityLevel(log.getMobilityLevel());
        existing.setFatigueLevel(log.getFatigueLevel());
        existing.setAdditionalNotes(log.getAdditionalNotes());
        
        return dailySymptomLogRepository.save(existing);
    }

    @Override
    public List<DailySymptomLog> getAllLogs() {
        return dailySymptomLogRepository.findAll();
    }

    private void checkForDeviations(DailySymptomLog log, PatientProfile patient) {
        // Get deviation rules for this surgery type
        List<DeviationRule> rules = deviationRuleService.getRulesBySurgery(patient.getSurgeryType());
        
        for (DeviationRule rule : rules) {
            if (!rule.getActive()) continue;
            
            // Check if this rule applies and threshold is exceeded
            boolean alertTriggered = false;
            String alertMessage = "";
            
            switch (rule.getParameter().toUpperCase()) {
                case "PAIN":
                    if (log.getPainLevel() > rule.getThreshold()) {
                        alertTriggered = true;
                        alertMessage = String.format("Pain level %d exceeds threshold %d", 
                                log.getPainLevel(), rule.getThreshold());
                    }
                    break;
                case "MOBILITY":
                    if (log.getMobilityLevel() < rule.getThreshold()) {
                        alertTriggered = true;
                        alertMessage = String.format("Mobility level %d below threshold %d", 
                                log.getMobilityLevel(), rule.getThreshold());
                    }
                    break;
                case "FATIGUE":
                    if (log.getFatigueLevel() > rule.getThreshold()) {
                        alertTriggered = true;
                        alertMessage = String.format("Fatigue level %d exceeds threshold %d", 
                                log.getFatigueLevel(), rule.getThreshold());
                    }
                    break;
            }
            
            if (alertTriggered) {
                ClinicalAlertRecord alert = ClinicalAlertRecord.builder()
                        .patientId(log.getPatientId())
                        .logId(log.getId())
                        .alertType(rule.getParameter().toUpperCase() + "_ALERT")
                        .severity(rule.getSeverity())
                        .message(alertMessage)
                        .resolved(false)
                        .build();
                
                clinicalAlertService.createAlert(alert);
            }
        }
    }
}