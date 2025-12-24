package com.example.demo.service;

import com.example.demo.model.PatientProfile;

import java.util.List;
import java.util.Optional;

public interface PatientProfileService {

    PatientProfile createPatient(PatientProfile patient);

    PatientProfile getPatientById(Long id);

    List<PatientProfile> getAllPatients();

    PatientProfile updatePatientStatus(Long id, boolean active);

    Optional<PatientProfile> findByPatientId(String patientId);
}
