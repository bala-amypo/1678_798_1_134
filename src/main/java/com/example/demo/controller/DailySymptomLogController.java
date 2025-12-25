package com.example.demo.controller;

import com.example.demo.model.DailySymptomLog;
import com.example.demo.service.DailySymptomLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/symptoms")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Symptom Logs", description = "Endpoints for managing daily symptom logs")
public class DailySymptomLogController {

    private final DailySymptomLogService dailySymptomLogService;

    @PostMapping
    @Operation(summary = "Create a new daily symptom log")
    public ResponseEntity<DailySymptomLog> createSymptomLog(@Valid @RequestBody DailySymptomLog log) {
        DailySymptomLog created = dailySymptomLogService.recordSymptomLog(log);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all symptom logs for a patient")
    public ResponseEntity<List<DailySymptomLog>> getLogsByPatient(@PathVariable Long patientId) {
        List<DailySymptomLog> logs = dailySymptomLogService.getLogsByPatient(patientId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/patient/{patientId}/range")
    @Operation(summary = "Get symptom logs for a patient within date range")
    public ResponseEntity<List<DailySymptomLog>> getLogsByPatientAndDateRange(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailySymptomLog> logs = dailySymptomLogService.getLogsByPatientAndDateRange(patientId, startDate, endDate);
        return ResponseEntity.ok(logs);
    }

    @PutMapping("/{logId}")
    @Operation(summary = "Update a symptom log")
    public ResponseEntity<DailySymptomLog> updateSymptomLog(
            @PathVariable Long logId,
            @Valid @RequestBody DailySymptomLog updates) {
        DailySymptomLog updated = dailySymptomLogService.updateSymptomLog(logId, updates);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{logId}")
    @Operation(summary = "Delete a symptom log")
    public ResponseEntity<Void> deleteSymptomLog(@PathVariable Long logId) {
        dailySymptomLogService.deleteLog(logId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{logId}")
    @Operation(summary = "Get symptom log by ID")
    public ResponseEntity<DailySymptomLog> getSymptomLogById(@PathVariable Long logId) {
        // Note: This method needs to be added to the service
        // For now, you'll need to add it to DailySymptomLogService
        // return ResponseEntity.ok(dailySymptomLogService.getLogById(logId));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}