package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
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
    public ResponseEntity<PatientProfile> createPatient(@RequestBody PatientProfile patient) {
        return ResponseEntity.ok(service.createPatient(patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientProfile> getPatientById(@PathVariable Long id) {
        PatientProfile patient = service.getPatientById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return ResponseEntity.ok(patient);
    }

    @GetMapping
    public ResponseEntity<List<PatientProfile>> getAllPatients() {
        return ResponseEntity.ok(service.getAllPatients());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PatientProfile> updatePatientStatus(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        return ResponseEntity.ok(service.updatePatientStatus(id, active));
    }

    @GetMapping("/lookup/{patientId}")
    public ResponseEntity<PatientProfile> findByPatientId(@PathVariable String patientId) {
        PatientProfile patient = service.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return ResponseEntity.ok(patient);
    }
}
