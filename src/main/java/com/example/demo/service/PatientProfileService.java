package com.example.demo.service;

import com.example.demo.model.PatientProfile;

import java.util.List;
import java.util.Optional;

public interface PatientProfileService {
    PatientProfile createPatient(PatientProfile profile);
    PatientProfile getPatientById(Long id);
    PatientProfile updatePatientStatus(Long id, boolean active);
    List<PatientProfile> getAllPatients();
    Optional<PatientProfile> findByPatientId(String patientId);
}