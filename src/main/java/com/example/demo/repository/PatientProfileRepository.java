package com.example.demo.repository;

import com.example.demo.model.PatientProfile;

import java.util.List;
import java.util.Optional;

public interface PatientProfileRepository {

    Optional<PatientProfile> findById(Long id);

    Optional<PatientProfile> findByPatientId(String patientId);

    List<PatientProfile> findAll();

    PatientProfile save(PatientProfile profile);
}
