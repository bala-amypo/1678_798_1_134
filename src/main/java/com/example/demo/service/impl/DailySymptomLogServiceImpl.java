package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DailySymptomLog;
import com.example.demo.repository.DailySymptomLogRepository;
import com.example.demo.repository.PatientProfileRepository;
import com.example.demo.service.ClinicalAlertService;
import com.example.demo.service.DailySymptomLogService;
import com.example.demo.service.DeviationRuleService;
import com.example.demo.service.RecoveryCurveService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service   // 🔴 THIS WAS MISSING
public class DailySymptomLogServiceImpl implements DailySymptomLogService {

    private final DailySymptomLogRepository dailySymptomLogRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final RecoveryCurveService recoveryCurveService;
    private final DeviationRuleService deviationRuleService;
    private final ClinicalAlertService clinicalAlertService;

    // 🔴 REQUIRED constructor (no @Autowired)
    public DailySymptomLogServiceImpl(
            DailySymptomLogRepository dailySymptomLogRepository,
            PatientProfileRepository patientProfileRepository,
            RecoveryCurveService recoveryCurveService,
            DeviationRuleService deviationRuleService,
            ClinicalAlertService clinicalAlertService
    ) {
        this.dailySymptomLogRepository = dailySymptomLogRepository;
        this.patientProfileRepository = patientProfileRepository;
        this.recoveryCurveService = recoveryCurveService;
        this.deviationRuleService = deviationRuleService;
        this.clinicalAlertService = clinicalAlertService;
    }

    @Override
    public DailySymptomLog recordSymptomLog(DailySymptomLog log) {
        if (log.getLogDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("future date");
        }

        patientProfileRepository.findById(log.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        dailySymptomLogRepository
                .findByPatientIdAndLogDate(log.getPatientId(), log.getLogDate())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Duplicate log");
                });

        return dailySymptomLogRepository.save(log);
    }

    @Override
    public List<DailySymptomLog> getLogsByPatient(Long patientId) {
        return dailySymptomLogRepository.findByPatientId(patientId);
    }

    @Override
    public Optional<DailySymptomLog> getLogById(Long id) {
        return dailySymptomLogRepository.findById(id);
    }

    @Override
    public DailySymptomLog updateSymptomLog(Long id, DailySymptomLog updated) {
        DailySymptomLog existing = dailySymptomLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found"));

        existing.setPainLevel(updated.getPainLevel());
        existing.setMobilityLevel(updated.getMobilityLevel());
        existing.setFatigueLevel(updated.getFatigueLevel());
        existing.setAdditionalNotes(updated.getAdditionalNotes());

        return dailySymptomLogRepository.save(existing);
    }
}
