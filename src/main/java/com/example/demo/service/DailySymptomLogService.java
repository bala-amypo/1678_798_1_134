package com.example.demo.service;

import com.example.demo.model.DailySymptomLog;

import java.util.List;

public interface DailySymptomLogService {

    DailySymptomLog recordSymptomLog(DailySymptomLog log);

    DailySymptomLog updateSymptomLog(Long id, DailySymptomLog updated);

    List<DailySymptomLog> getLogsByPatient(Long patientId);
}
