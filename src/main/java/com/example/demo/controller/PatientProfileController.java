package com.example.demo.controller;

import com.example.demo.model.PatientProfile;
import com.example.demo.service.PatientProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients")
public class PatientProfileController {

    private final PatientProfileService service;

    public PatientProfileController(PatientProfileService service) {
        this.service = service;
    }

    @PostMapping
    public PatientProfile create(@RequestBody PatientProfile profile) {
        return service.createPatient(profile);
    }

    @GetMapping("/{id}")
    public PatientProfile getById(@PathVariable Long id) {
        return service.getPatientById(id);
    }

    @GetMapping
    public List<PatientProfile> getAll() {
        return service.getAllPatients();
    }

    @PutMapping("/{id}/status")
    public PatientProfile updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return service.updatePatientStatus(id, active);
    }

    @GetMapping("/lookup/{patientId}")
    public PatientProfile lookup(@PathVariable String patientId) {
        return service.findByPatientId(patientId).orElse(null);
    }
}
