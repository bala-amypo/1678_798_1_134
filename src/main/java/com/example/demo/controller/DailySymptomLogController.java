package com.example.demo.controller;

import com.example.demo.model.DailySymptomLog;
import com.example.demo.service.DailySymptomLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symptom-logs")
public class DailySymptomLogController {

    private final DailySymptomLogService service;

    public DailySymptomLogController(DailySymptomLogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DailySymptomLog> create(
            @RequestBody DailySymptomLog log) {
        return ResponseEntity.ok(service.recordSymptomLog(log));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailySymptomLog> update(
            @PathVariable Long id,
            @RequestBody DailySymptomLog log) {
        return ResponseEntity.ok(service.updateSymptomLog(id, log));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DailySymptomLog>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(service.getLogsByPatient(patientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailySymptomLog> getById(
            @PathVariable Long id) {
        return service.getLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DailySymptomLog>> getAll() {
        return ResponseEntity.ok(service.getAllLogs());
    }
}
