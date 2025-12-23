package com.example.demo.service;

import com.example.demo.model.DailySymptomLog;

import java.util.List;

public interface DailySymptomLogService {

    DailySymptomLog recordSymptomLog(DailySymptomLog log);

    DailySymptomLog updateSymptomLog(long id, DailySymptomLog log);

    List<DailySymptomLog> getLogsByPatient(long patientId);
}
