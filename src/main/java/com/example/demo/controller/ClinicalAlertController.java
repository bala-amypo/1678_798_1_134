package com.example.demo.controller;

import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.service.ClinicalAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class ClinicalAlertController {

    private final ClinicalAlertService clinicalAlertService;

    @GetMapping
    public ResponseEntity<List<ClinicalAlertRecord>> getAll() {
        return ResponseEntity.ok(clinicalAlertService.getAllAlerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalAlertRecord> getById(
            @PathVariable Long id) {
        return clinicalAlertService.getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ClinicalAlertRecord>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                clinicalAlertService.getAlertsByPatient(patientId));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<ClinicalAlertRecord> resolve(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                clinicalAlertService.resolveAlert(id));
    }
}
