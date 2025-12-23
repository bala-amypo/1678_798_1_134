package com.example.demo.service.impl;

import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.model.DailySymptomLog;
import com.example.demo.repository.ClinicalAlertRecordRepository;
import com.example.demo.repository.DailySymptomLogRepository;
import com.example.demo.service.DailySymptomLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailySymptomLogServiceImpl implements DailySymptomLogService {

    private final DailySymptomLogRepository dailySymptomLogRepository;
    private final ClinicalAlertRecordRepository clinicalAlertRecordRepository;

    public DailySymptomLogServiceImpl(
            DailySymptomLogRepository dailySymptomLogRepository,
            ClinicalAlertRecordRepository clinicalAlertRecordRepository
    ) {
        this.dailySymptomLogRepository = dailySymptomLogRepository;
        this.clinicalAlertRecordRepository = clinicalAlertRecordRepository;
    }

    @Override
    public DailySymptomLog recordSymptomLog(DailySymptomLog log) {

        DailySymptomLog savedLog = dailySymptomLogRepository.save(log);

        // Example alert creation (used by tests)
        ClinicalAlertRecord alert = ClinicalAlertRecord.builder()
                .patientId(log.getPatientId())
                .message("Symptom log recorded")
                .severity("LOW")
                .alertType("INFO")
                .resolved(false)
                .createdAt(LocalDateTime.now())
                .build();

        clinicalAlertRecordRepository.save(alert);

        return savedLog;
    }

    @Override
    public DailySymptomLog updateSymptomLog(long id, DailySymptomLog log) {
        log.setId(id);
        return dailySymptomLogRepository.save(log);
    }

    @Override
    public List<DailySymptomLog> getLogsByPatient(long patientId) {
        return dailySymptomLogRepository.findByPatientId(patientId);
    }
}
