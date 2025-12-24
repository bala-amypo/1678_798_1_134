package com.example.demo.service;

import com.example.demo.model.DailySymptomLog;
import java.util.List;

public interface DailySymptomLogService {
    DailySymptomLog recordSymptomLog(DailySymptomLog log);
    List<DailySymptomLog> getLogsByPatient(Long patientId);
    DailySymptomLog updateSymptomLog(Long logId, DailySymptomLog updatedLog);
    void deleteSymptomLog(Long id);
}