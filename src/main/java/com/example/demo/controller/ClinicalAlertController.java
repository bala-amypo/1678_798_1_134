package com.example.demo.controller;

import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.service.ClinicalAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clinical Alerts", description = "Endpoints for managing clinical alerts")
public class ClinicalAlertController {

    private final ClinicalAlertService clinicalAlertService;

    @GetMapping
    @Operation(summary = "Get all clinical alerts")
    public ResponseEntity<List<ClinicalAlertRecord>> getAllAlerts() {
        List<ClinicalAlertRecord> alerts = clinicalAlertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/unresolved")
    @Operation(summary = "Get all unresolved clinical alerts")
    public ResponseEntity<List<ClinicalAlertRecord>> getUnresolvedAlerts() {
        List<ClinicalAlertRecord> alerts = clinicalAlertService.getUnresolvedAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get clinical alerts by patient ID")
    public ResponseEntity<List<ClinicalAlertRecord>> getAlertsByPatient(@PathVariable Long patientId) {
        List<ClinicalAlertRecord> alerts = clinicalAlertService.getAlertsByPatient(patientId);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/patient/{patientId}/unresolved")
    @Operation(summary = "Get unresolved clinical alerts by patient ID")
    public ResponseEntity<List<ClinicalAlertRecord>> getUnresolvedAlertsByPatient(@PathVariable Long patientId) {
        List<ClinicalAlertRecord> alerts = clinicalAlertService.getUnresolvedAlertsByPatient(patientId);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get clinical alert by ID")
    public ResponseEntity<ClinicalAlertRecord> getAlertById(@PathVariable Long id) {
        return clinicalAlertService.getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new clinical alert")
    public ResponseEntity<ClinicalAlertRecord> createAlert(@Valid @RequestBody ClinicalAlertRecord alert) {
        ClinicalAlertRecord created = clinicalAlertService.createAlert(alert);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/resolve")
    @Operation(summary = "Resolve a clinical alert")
    public ResponseEntity<ClinicalAlertRecord> resolveAlert(@PathVariable Long id, 
                                                          @RequestParam Long userId,
                                                          @RequestParam(required = false) String notes) {
        ClinicalAlertRecord resolved = clinicalAlertService.resolveAlert(id, userId, notes);
        return ResponseEntity.ok(resolved);
    }

    @PatchMapping("/{id}/acknowledge")
    @Operation(summary = "Acknowledge a clinical alert")
    public ResponseEntity<ClinicalAlertRecord> acknowledgeAlert(@PathVariable Long id, 
                                                              @RequestParam Long userId) {
        ClinicalAlertRecord acknowledged = clinicalAlertService.acknowledgeAlert(id, userId);
        return ResponseEntity.ok(acknowledged);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a clinical alert")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        clinicalAlertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get alert statistics")
    public ResponseEntity<?> getAlertStatistics() {
        return ResponseEntity.ok(clinicalAlertService.getAlertStatistics());
    }

    @GetMapping("/urgent")
    @Operation(summary = "Get alerts requiring immediate attention")
    public ResponseEntity<List<ClinicalAlertRecord>> getAlertsRequiringImmediateAttention() {
        List<ClinicalAlertRecord> alerts = clinicalAlertService.getAlertsRequiringImmediateAttention();
        return ResponseEntity.ok(alerts);
    }

    @PatchMapping("/{id}/escalate")
    @Operation(summary = "Escalate a clinical alert")
    public ResponseEntity<ClinicalAlertRecord> escalateAlert(@PathVariable Long id) {
        ClinicalAlertRecord escalated = clinicalAlertService.escalateAlert(id);
        return ResponseEntity.ok(escalated);
    }

    @GetMapping("/severity/{severity}")
    @Operation(summary = "Get alerts by severity")
    public ResponseEntity<List<ClinicalAlertRecord>> getAlertsBySeverity(@PathVariable String severity) {
        List<ClinicalAlertRecord> alerts = clinicalAlertService.getAlertsBySeverity(severity);
        return ResponseEntity.ok(alerts);
    }
}