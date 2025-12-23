package com.example.demo.repository;

import com.example.demo.model.PatientProfile;

import java.util.Optional;

public interface PatientProfileRepository {

    Optional<PatientProfile> findByEmail(String email);

    boolean existsById(Long id);

    void deleteById(Long id);

    PatientProfile save(PatientProfile profile);
}
