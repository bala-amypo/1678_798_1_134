package com.example.demo.controller;

import com.example.demo.model.PatientProfile;
import com.example.demo.service.PatientProfileService;
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
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Patient Management", description = "Endpoints for managing patient profiles")
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    @PostMapping
    @Operation(summary = "Create a new patient profile")
    public ResponseEntity<PatientProfile> createPatient(@Valid @RequestBody PatientProfile patientProfile) {
        PatientProfile created = patientProfileService.createPatient(patientProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<PatientProfile>> getAllPatients() {
        List<PatientProfile> patients = patientProfileService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<PatientProfile> getPatientById(@PathVariable Long id) {
        PatientProfile patient = patientProfileService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/patient-id/{patientId}")
    @Operation(summary = "Get patient by patient ID")
    public ResponseEntity<PatientProfile> getPatientByPatientId(@PathVariable String patientId) {
        return patientProfileService.findByPatientId(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient")
    public ResponseEntity<PatientProfile> updatePatient(@PathVariable Long id, 
                                                       @Valid @RequestBody PatientProfile updates) {
        PatientProfile updated = patientProfileService.updatePatient(id, updates);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update patient status")
    public ResponseEntity<PatientProfile> updatePatientStatus(@PathVariable Long id, 
                                                             @RequestParam Boolean active) {
        PatientProfile updated = patientProfileService.updatePatientStatus(id, active);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientProfileService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}