package com.example.demo.service;

import com.example.demo.model.DailySymptomLog;

import java.util.List;
import java.util.Optional;

public interface DailySymptomLogService {
    DailySymptomLog recordSymptomLog(DailySymptomLog log);
    DailySymptomLog createLog(DailySymptomLog log);
    List<DailySymptomLog> getLogsByPatient(Long patientId);
    List<DailySymptomLog> getLogsByPatientId(Long patientId);
    Optional<DailySymptomLog> getLogById(Long id);
    DailySymptomLog updateSymptomLog(Long id, DailySymptomLog log);
    List<DailySymptomLog> getAllLogs();
}
package com.example.demo.service;

import com.example.demo.model.DailySymptomLog;

public interface DailySymptomLogService {
    DailySymptomLog saveDailyLog(DailySymptomLog log);
}
