package com.example.demo.controller;

import com.example.demo.model.PatientProfile;
import com.example.demo.service.PatientProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient Management", description = "Patient profile management APIs")
public class PatientController {

    @Autowired
    private PatientProfileService patientProfileService;

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve all patient profiles")
    public ResponseEntity<List<PatientProfile>> getAllPatients() {
        List<PatientProfile> patients = patientProfileService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieve a patient profile by ID")
    public ResponseEntity<PatientProfile> getPatientById(@PathVariable Long id) {
        PatientProfile patient = patientProfileService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @PostMapping
    @Operation(summary = "Create patient", description = "Create a new patient profile")
    public ResponseEntity<PatientProfile> createPatient(@RequestBody PatientProfile patient) {
        PatientProfile createdPatient = patientProfileService.createPatient(patient);
        return ResponseEntity.ok(createdPatient);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the patient service is running")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Patient service is running");
    }
}