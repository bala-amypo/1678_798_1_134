package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PatientProfile;
import com.example.demo.repository.PatientProfileRepository;
import com.example.demo.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientProfileServiceImpl implements PatientProfileService {
    
    private final PatientProfileRepository patientProfileRepository;
    
    @Override
    @Transactional
    public PatientProfile createPatient(PatientProfile patient) {
        // Validate required fields
        if (patient.getPatientId() == null || patient.getPatientId().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        
        if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Check if patient ID already exists
        if (patientProfileRepository.findByPatientId(patient.getPatientId()).isPresent()) {
            throw new IllegalArgumentException("Patient ID already exists: " + patient.getPatientId());
        }
        
        return patientProfileRepository.save(patient);
    }
    
    @Override
    public PatientProfile getPatientById(Long id) {
        return patientProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }
    
    @Override
    public Optional<PatientProfile> findByPatientId(String patientId) {
        return patientProfileRepository.findByPatientId(patientId);
    }
    
    @Override
    public List<PatientProfile> getAllPatients() {
        return patientProfileRepository.findAll();
    }
    
    @Override
    @Transactional
    public PatientProfile updatePatientStatus(Long id, Boolean active) {
        PatientProfile patient = getPatientById(id);
        patient.setActive(active);
        return patientProfileRepository.save(patient);
    }
    
    @Override
    @Transactional
    public void deletePatient(Long id) {
        PatientProfile patient = getPatientById(id);
        patientProfileRepository.delete(patient);
    }
}