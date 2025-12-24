package com.example.demo.controller;

import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.service.ClinicalAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class ClinicalAlertController {

    private final ClinicalAlertService clinicalAlertService;

    // 🔴 REQUIRED constructor
    public ClinicalAlertController(ClinicalAlertService clinicalAlertService) {
        this.clinicalAlertService = clinicalAlertService;
    }

    @PostMapping
    public ClinicalAlertRecord create(@RequestBody ClinicalAlertRecord alert) {
        return clinicalAlertService.createAlert(alert);
    }

    @PutMapping("/{id}/resolve")
    public ClinicalAlertRecord resolve(@PathVariable Long id) {
        return clinicalAlertService.resolveAlert(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<ClinicalAlertRecord> getByPatient(@PathVariable Long patientId) {
        return clinicalAlertService.getAlertsByPatient(patientId);
    }

    @GetMapping("/{id}")
    public ClinicalAlertRecord getById(@PathVariable Long id) {
        return clinicalAlertService.getAlertById(id).orElse(null);
    }

    @GetMapping
    public List<ClinicalAlertRecord> getAll() {
        return clinicalAlertService.getAllAlerts();
    }
}
