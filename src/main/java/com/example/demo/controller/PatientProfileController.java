package com.example.demo.controller;

import com.example.demo.model.PatientProfile;
import com.example.demo.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    @PostMapping
    public ResponseEntity<PatientProfile> create(
            @RequestBody PatientProfile profile) {
        return ResponseEntity.ok(patientProfileService.createPatient(profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientProfile> getById(@PathVariable Long id) {
        return ResponseEntity.ok(patientProfileService.getPatientById(id));
    }

    @GetMapping
    public ResponseEntity<List<PatientProfile>> getAll() {
        return ResponseEntity.ok(patientProfileService.getAllPatients());
    }

    @GetMapping("/search/{patientId}")
    public ResponseEntity<PatientProfile> findByPatientId(
            @PathVariable String patientId) {
        return patientProfileService.findByPatientId(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PatientProfile> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(
                patientProfileService.updatePatientStatus(id, active));
    }
}
