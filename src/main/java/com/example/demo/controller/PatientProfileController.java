package com.example.demo.controller;

import com.example.demo.model.PatientProfile;
import com.example.demo.service.PatientProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientProfileController {

    private final PatientProfileService service;

    public PatientProfileController(PatientProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PatientProfile> create(
            @RequestBody PatientProfile patient) {
        return ResponseEntity.ok(service.createPatient(patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientProfile> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPatientById(id));
    }

    @GetMapping
    public ResponseEntity<List<PatientProfile>> getAll() {
        return ResponseEntity.ok(service.getAllPatients());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PatientProfile> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(service.updatePatientStatus(id, active));
    }

    @GetMapping("/lookup/{patientId}")
    public ResponseEntity<PatientProfile> findByPatientId(
            @PathVariable String patientId) {
        return service.findByPatientId(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
