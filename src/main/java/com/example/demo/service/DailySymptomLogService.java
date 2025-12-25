package com.example.demo.service;

import com.example.demo.model.DailySymptomLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DailySymptomLogService {
    DailySymptomLog recordSymptomLog(DailySymptomLog log);
    DailySymptomLog updateSymptomLog(Long logId, DailySymptomLog updates);
    Optional<DailySymptomLog> getLogById(Long logId);
    List<DailySymptomLog> getLogsByPatient(Long patientId);
    List<DailySymptomLog> getLogsByPatientAndDateRange(Long patientId, LocalDate start, LocalDate end);
    List<DailySymptomLog> getRecentLogs(Long patientId, int limit);
    void deleteLog(Long logId);
    boolean hasLogForDate(Long patientId, LocalDate date);
    Map<String, Double> calculatePatientAverages(Long patientId);
    List<DailySymptomLog> getLogsWithHighPain(Long patientId, Integer threshold);
    List<LocalDate> getMissingLogDates(Long patientId, LocalDate startDate, LocalDate endDate);
    DailySymptomLog getOrCreateTodayLog(Long patientId, Long recordedBy);
}