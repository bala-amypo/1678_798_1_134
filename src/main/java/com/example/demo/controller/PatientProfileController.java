package com.example.demo.controller;

import com.example.demo.model.PatientProfile;
import com.example.demo.service.PatientProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient Profiles", description = "Patient profile management")
public class PatientProfileController {
    private final PatientProfileService patientProfileService;

    public PatientProfileController(PatientProfileService patientProfileService) {
        this.patientProfileService = patientProfileService;
    }

    @PostMapping
    public ResponseEntity<PatientProfile> createPatient(@Valid @RequestBody PatientProfile patient) {
        PatientProfile created = patientProfileService.createPatient(patient);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientProfile> getPatientById(@PathVariable Long id) {
        PatientProfile patient = patientProfileService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping
    public ResponseEntity<List<PatientProfile>> getAllPatients() {
        List<PatientProfile> patients = patientProfileService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PatientProfile> updatePatientStatus(@PathVariable Long id, @RequestParam boolean active) {
        PatientProfile updated = patientProfileService.updatePatientStatus(id, active);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/lookup/{patientId}")
    public ResponseEntity<PatientProfile> lookupByPatientId(@PathVariable String patientId) {
        Optional<PatientProfile> patient = patientProfileService.findByPatientId(patientId);
        return patient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}