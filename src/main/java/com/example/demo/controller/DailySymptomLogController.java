package com.example.demo.controller;

import com.example.demo.model.DailySymptomLog;
import com.example.demo.service.DailySymptomLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symptoms")
@RequiredArgsConstructor
public class DailySymptomLogController {

    private final DailySymptomLogService dailySymptomLogService;

    @PostMapping
    public ResponseEntity<DailySymptomLog> create(
            @RequestBody DailySymptomLog log) {
        return ResponseEntity.ok(
                dailySymptomLogService.recordSymptomLog(log));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailySymptomLog> update(
            @PathVariable Long id,
            @RequestBody DailySymptomLog log) {
        return ResponseEntity.ok(
                dailySymptomLogService.updateSymptomLog(id, log));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DailySymptomLog>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                dailySymptomLogService.getLogsByPatient(patientId));
    }
}
