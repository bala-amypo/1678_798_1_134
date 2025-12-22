package com.example.demo.service.impl;

import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.model.DailySymptomLog;
import com.example.demo.repository.ClinicalAlertRecordRepository;
import com.example.demo.repository.DailySymptomLogRepository;
import com.example.demo.service.DailySymptomLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
    public DailySymptomLog saveDailyLog(DailySymptomLog log) {

        // Ensure date is set
        if (log.getLogDate() == null) {
            log.setLogDate(LocalDate.now());
        }

        DailySymptomLog savedLog = dailySymptomLogRepository.save(log);

        // Simple deviation rule (example)
        if (log.getPainLevel() > 7 || log.getFatigueLevel() > 7) {

            ClinicalAlertRecord alert = ClinicalAlertRecord.builder()
                    .alertType("SYMPTOM_DEVIATION")
                    .message("High symptom levels detected")
                    .resolved(false)
                    .build();

            clinicalAlertRecordRepository.save(alert);
        }

        return savedLog;
    }
}
