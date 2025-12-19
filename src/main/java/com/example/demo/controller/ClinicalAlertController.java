package com.example.demo.controller;

import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.service.ClinicalAlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Clinical Alerts")
public class ClinicalAlertController {

    private final ClinicalAlertService service;

    public ClinicalAlertController(ClinicalAlertService service) {
        this.service = service;
    }

    @PostMapping
    public ClinicalAlertRecord create(@RequestBody ClinicalAlertRecord alert) {
        return service.createAlert(alert);
    }

    @PutMapping("/{id}/resolve")
    public ClinicalAlertRecord resolve(@PathVariable Long id) {
        return service.resolveAlert(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<ClinicalAlertRecord> byPatient(@PathVariable Long patientId) {
        return service.getAlertsByPatient(patientId);
    }

    @GetMapping
    public List<ClinicalAlertRecord> getAll() {
        return service.getAllAlerts();
    }
}
