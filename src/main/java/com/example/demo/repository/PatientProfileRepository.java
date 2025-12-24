package com.example.demo.repository;

import com.example.demo.model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    Optional<PatientProfile> findByPatientId(String patientId);
    List<PatientProfile> findByActiveTrue();
    List<PatientProfile> findBySurgeryType(String surgeryType);
}