package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class DailySymptomLogServiceImpl
        implements DailySymptomLogService {

    private final DailySymptomLogRepository logRepo;
    private final PatientProfileRepository patientRepo;
    private final RecoveryCurveService recoveryCurveService;
    private final DeviationRuleService deviationRuleService;
    private final ClinicalAlertService alertService;

    // REQUIRED constructor (TestNG)
    public DailySymptomLogServiceImpl(
            DailySymptomLogRepository logRepo,
            PatientProfileRepository patientRepo,
            RecoveryCurveService recoveryCurveService,
            DeviationRuleService deviationRuleService,
            ClinicalAlertService alertService) {

        this.logRepo = logRepo;
        this.patientRepo = patientRepo;
        this.recoveryCurveService = recoveryCurveService;
        this.deviationRuleService = deviationRuleService;
        this.alertService = alertService;
    }

    @Override
    public DailySymptomLog recordSymptomLog(DailySymptomLog log) {

        PatientProfile patient = patientRepo
                .findById(log.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));

        if (log.getLogDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("future date");
        }

        if (logRepo.findByPatientIdAndLogDate(
                log.getPatientId(), log.getLogDate()).isPresent()) {
            throw new IllegalArgumentException("Duplicate log");
        }

        DailySymptomLog saved = logRepo.save(log);

        long day =
                ChronoUnit.DAYS.between(
                        patient.getCreatedAt().toLocalDate(),
                        log.getLogDate());

        recoveryCurveService
                .getCurveByDayAndSurgery(
                        patient.getSurgeryType(),
                        (int) day)
                .ifPresent(curve -> {

                    deviationRuleService.getActiveRules()
                            .forEach(rule -> {

                                if (!rule.getParameter().equalsIgnoreCase("PAIN")) return;

                                int deviation =
                                        saved.getPainLevel()
                                                - curve.getExpectedPainLevel();

                                if (deviation > rule.getThreshold()) {

                                    alertService.createAlert(
                                            ClinicalAlertRecord.builder()
                                                    .patientId(saved.getPatientId())
                                                    .logId(saved.getId())
                                                    .alertType("PAIN_SPIKE")
                                                    .severity(rule.getSeverity())
                                                    .message("Deviation detected")
                                                    .resolved(false)
                                                    .build()
                                    );
                                }
                            });
                });

        return saved;
    }

    @Override
    public List<DailySymptomLog> getLogsByPatient(Long patientId) {
        patientRepo.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));
        return logRepo.findByPatientId(patientId);
    }

    @Override
    public Optional<DailySymptomLog> getLogById(Long id) {
        return logRepo.findById(id);
    }

    @Override
    public DailySymptomLog updateSymptomLog(Long id, DailySymptomLog updated) {

        DailySymptomLog existing = logRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Log not found"));

        existing.setPainLevel(updated.getPainLevel());
        existing.setMobilityLevel(updated.getMobilityLevel());
        existing.setFatigueLevel(updated.getFatigueLevel());
        existing.setAdditionalNotes(updated.getAdditionalNotes());

        return logRepo.save(existing);
    }

    @Override
    public List<DailySymptomLog> getAllLogs() {
        return logRepo.findAll();
    }
}
