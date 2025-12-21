package com.example.demo.controller;

import com.example.demo.model.DailySymptomLog;
import com.example.demo.service.DailySymptomLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/symptom-logs")
@Tag(name = "Daily Symptom Logs", description = "Daily symptom log management")
public class DailySymptomLogController {
    private final DailySymptomLogService dailySymptomLogService;

    public DailySymptomLogController(DailySymptomLogService dailySymptomLogService) {
        this.dailySymptomLogService = dailySymptomLogService;
    }

    @PostMapping
    public ResponseEntity<DailySymptomLog> recordSymptomLog(@Valid @RequestBody DailySymptomLog log) {
        DailySymptomLog recorded = dailySymptomLogService.recordSymptomLog(log);
        return ResponseEntity.ok(recorded);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailySymptomLog> updateSymptomLog(@PathVariable Long id, @Valid @RequestBody DailySymptomLog log) {
        DailySymptomLog updated = dailySymptomLogService.updateSymptomLog(id, log);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DailySymptomLog>> getLogsByPatient(@PathVariable Long patientId) {
        List<DailySymptomLog> logs = dailySymptomLogService.getLogsByPatient(patientId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailySymptomLog> getLogById(@PathVariable Long id) {
        Optional<DailySymptomLog> log = dailySymptomLogService.getLogById(id);
        return log.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DailySymptomLog>> getAllLogs() {
        List<DailySymptomLog> logs = dailySymptomLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}