package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PatientProfile;
import com.example.demo.repository.PatientProfileRepository;
import com.example.demo.service.PatientProfileService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientProfileRepository repository;

    @Override
    public PatientProfile createPatient(PatientProfile profile) {
        return repository.save(profile);
    }

    @Override
    public PatientProfile getPatientById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));
    }

    @Override
    public List<PatientProfile> getAllPatients() {
        return repository.findAll();
    }

    @Override
    public Optional<PatientProfile> findByPatientId(String patientId) {
        return repository.findByPatientId(patientId);
    }

    @Override
    public PatientProfile updatePatientStatus(Long id, boolean active) {
        PatientProfile profile = getPatientById(id);
        profile.setActive(active);
        return repository.save(profile);
    }
}
