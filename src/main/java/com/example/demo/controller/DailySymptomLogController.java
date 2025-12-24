package com.example.demo.controller;

import com.example.demo.model.DailySymptomLog;
import com.example.demo.service.DailySymptomLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symptom-logs")
public class DailySymptomLogController {

    private final DailySymptomLogService service;

    // 🔴 REQUIRED constructor injection
    public DailySymptomLogController(DailySymptomLogService service) {
        this.service = service;
    }

    @PostMapping
    public DailySymptomLog record(@RequestBody DailySymptomLog log) {
        return service.recordSymptomLog(log);
    }

    @PutMapping("/{id}")
    public DailySymptomLog update(
            @PathVariable Long id,
            @RequestBody DailySymptomLog log
    ) {
        return service.updateSymptomLog(id, log);
    }

    @GetMapping("/patient/{patientId}")
    public List<DailySymptomLog> getByPatient(@PathVariable Long patientId) {
        return service.getLogsByPatient(patientId);
    }

    @GetMapping("/{id}")
    public DailySymptomLog getById(@PathVariable Long id) {
        return service.getLogById(id).orElse(null);
    }

    @GetMapping
    public List<DailySymptomLog> getAllLogs() {
        return service.getAllLogs();
    }
}
