package com.example.demo.controller;

import com.example.demo.model.DailySymptomLog;
import com.example.demo.service.DailySymptomLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Daily Symptom Logs", description = "Daily symptom log management endpoints")
public class DailySymptomLogController {

    private final DailySymptomLogService dailySymptomLogService;

    @Operation(summary = "Record a new symptom log", 
               description = "Creates a new daily symptom log for a patient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Symptom log created successfully",
                    content = @Content(schema = @Schema(implementation = DailySymptomLog.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or duplicate log"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<DailySymptomLog> recordSymptomLog(
            @Parameter(description = "Symptom log details", required = true)
            @Valid @RequestBody DailySymptomLog log) {
        
        log.info("Recording symptom log for patient ID: {}", log.getPatientId());
        
        DailySymptomLog recordedLog = dailySymptomLogService.recordSymptomLog(log);
        
        log.info("Symptom log recorded successfully with ID: {}", recordedLog.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(recordedLog);
    }

    @Operation(summary = "Get symptom logs by patient", 
               description = "Returns all symptom logs for a specific patient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Symptom logs retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DailySymptomLog.class)))),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<DailySymptomLog>> getLogsByPatient(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable Long patientId) {
        
        log.info("Fetching symptom logs for patient ID: {}", patientId);
        
        List<DailySymptomLog> logs = dailySymptomLogService.getLogsByPatient(patientId);
        
        return ResponseEntity.ok(logs);
    }

    @Operation(summary = "Get symptom log by ID", 
               description = "Returns a single symptom log by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Symptom log found",
                    content = @Content(schema = @Schema(implementation = DailySymptomLog.class))),
        @ApiResponse(responseCode = "404", description = "Symptom log not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<DailySymptomLog> getLogById(
            @Parameter(description = "Symptom log ID", required = true)
            @PathVariable Long id) {
        
        log.info("Fetching symptom log with ID: {}", id);
        
        // Note: In a real app, you would have a service method getLogById
        // For now, we'll get all logs and filter (inefficient but works for demo)
        throw new UnsupportedOperationException("Implement getLogById service method");
    }

    @Operation(summary = "Update symptom log", 
               description = "Updates an existing symptom log")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Symptom log updated",
                    content = @Content(schema = @Schema(implementation = DailySymptomLog.class))),
        @ApiResponse(responseCode = "404", description = "Symptom log not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<DailySymptomLog> updateSymptomLog(
            @Parameter(description = "Symptom log ID", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Updated symptom log details", required = true)
            @Valid @RequestBody DailySymptomLog logDetails) {
        
        log.info("Updating symptom log with ID: {}", id);
        
        DailySymptomLog updatedLog = dailySymptomLogService.updateSymptomLog(id, logDetails);
        
        log.info("Symptom log updated successfully for ID: {}", id);
        
        return ResponseEntity.ok(updatedLog);
    }

    @Operation(summary = "Delete symptom log", 
               description = "Deletes a symptom log by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Symptom log deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Symptom log not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<Void> deleteSymptomLog(
            @Parameter(description = "Symptom log ID", required = true)
            @PathVariable Long id) {
        
        log.info("Deleting symptom log with ID: {}", id);
        
        dailySymptomLogService.deleteSymptomLog(id);
        
        log.info("Symptom log deleted successfully for ID: {}", id);
        
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get symptom log by date", 
               description = "Returns symptom log for a specific patient and date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Symptom log found",
                    content = @Content(schema = @Schema(implementation = DailySymptomLog.class))),
        @ApiResponse(responseCode = "404", description = "Symptom log not found")
    })
    @GetMapping("/patient/{patientId}/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<DailySymptomLog> getLogByPatientAndDate(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable Long patientId,
            
            @Parameter(description = "Log date in YYYY-MM-DD format", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        log.info("Fetching symptom log for patient ID: {} on date: {}", patientId, date);
        
        // Note: In a real app, you would have a service method getLogByPatientAndDate
        List<DailySymptomLog> allLogs = dailySymptomLogService.getLogsByPatient(patientId);
        DailySymptomLog log = allLogs.stream()
                .filter(l -> date.equals(l.getLogDate()))
                .findFirst()
                .orElse(null);
        
        if (log == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(log);
    }

    @Operation(summary = "Get recent symptom logs", 
               description = "Returns recent symptom logs for a patient (last N days)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recent symptom logs retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DailySymptomLog.class))))
    })
    @GetMapping("/patient/{patientId}/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<DailySymptomLog>> getRecentLogs(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable Long patientId,
            
            @Parameter(description = "Number of days to look back", defaultValue = "7")
            @RequestParam(defaultValue = "7") int days) {
        
        log.info("Fetching recent symptom logs for patient ID: {} for last {} days", patientId, days);
        
        List<DailySymptomLog> allLogs = dailySymptomLogService.getLogsByPatient(patientId);
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        
        List<DailySymptomLog> recentLogs = allLogs.stream()
                .filter(l -> l.getLogDate() != null && !l.getLogDate().isBefore(cutoffDate))
                .sorted((a, b) -> b.getLogDate().compareTo(a.getLogDate())) // Newest first
                .toList();
        
        return ResponseEntity.ok(recentLogs);
    }

    @Operation(summary = "Get symptom logs summary", 
               description = "Returns summary statistics for a patient's symptom logs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Summary statistics retrieved")
    })
    @GetMapping("/patient/{patientId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<Map<String, Object>> getLogsSummary(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable Long patientId) {
        
        log.info("Fetching symptom logs summary for patient ID: {}", patientId);
        
        List<DailySymptomLog> allLogs = dailySymptomLogService.getLogsByPatient(patientId);
        
        Map<String, Object> summary = new HashMap<>();
        
        // Calculate averages
        double avgPain = allLogs.stream()
                .filter(l -> l.getPainLevel() != null)
                .mapToInt(DailySymptomLog::getPainLevel)
                .average()
                .orElse(0.0);
        
        double avgMobility = allLogs.stream()
                .filter(l -> l.getMobilityLevel() != null)
                .mapToInt(DailySymptomLog::getMobilityLevel)
                .average()
                .orElse(0.0);
        
        double avgFatigue = allLogs.stream()
                .filter(l -> l.getFatigueLevel() != null)
                .mapToInt(DailySymptomLog::getFatigueLevel)
                .average()
                .orElse(0.0);
        
        // Find trends
        List<DailySymptomLog> sortedLogs = allLogs.stream()
                .filter(l -> l.getLogDate() != null)
                .sorted(Comparator.comparing(DailySymptomLog::getLogDate))
                .toList();
        
        String painTrend = "stable";
        if (sortedLogs.size() >= 2) {
            DailySymptomLog first = sortedLogs.get(0);
            DailySymptomLog last = sortedLogs.get(sortedLogs.size() - 1);
            if (first.getPainLevel() != null && last.getPainLevel() != null) {
                painTrend = last.getPainLevel() < first.getPainLevel() ? "improving" : 
                           last.getPainLevel() > first.getPainLevel() ? "worsening" : "stable";
            }
        }
        
        summary.put("totalLogs", allLogs.size());
        summary.put("averagePain", Math.round(avgPain * 10.0) / 10.0);
        summary.put("averageMobility", Math.round(avgMobility * 10.0) / 10.0);
        summary.put("averageFatigue", Math.round(avgFatigue * 10.0) / 10.0);
        summary.put("painTrend", painTrend);
        summary.put("lastLogDate", allLogs.stream()
                .map(DailySymptomLog::getLogDate)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(null));
        
        return ResponseEntity.ok(summary);
    }
    
    // Helper method to create response map
    private Map<String, Object> createResponse(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}