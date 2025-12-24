package com.example.demo.service;

import com.example.demo.model.DailySymptomLog;

import java.util.List;
import java.util.Optional;

public interface DailySymptomLogService {

    DailySymptomLog recordSymptomLog(DailySymptomLog log);

    List<DailySymptomLog> getLogsByPatient(Long patientId);

    Optional<DailySymptomLog> getLogById(Long id);

    DailySymptomLog updateSymptomLog(Long id, DailySymptomLog log);
}
