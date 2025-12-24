package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PatientProfile;
import com.example.demo.repository.PatientProfileRepository;
import com.example.demo.service.PatientProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientProfileRepository repository;

    public PatientProfileServiceImpl(PatientProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public PatientProfile createPatient(PatientProfile patient) {

        repository.findByEmail(patient.getEmail()).ifPresent(p -> {
            throw new IllegalArgumentException("Email already exists");
        });

        patient.setActive(true);
        return repository.save(patient);
    }

    @Override
    public Optional<PatientProfile> getPatientById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<PatientProfile> getAllPatients() {
        return repository.findAll();
    }

    @Override
    public PatientProfile updatePatientStatus(Long id, boolean active) {
        PatientProfile patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        patient.setActive(active);
        return repository.save(patient);
    }

    @Override
    public Optional<PatientProfile> findByPatientId(String patientId) {
        return repository.findByPatientId(patientId);
    }
}
