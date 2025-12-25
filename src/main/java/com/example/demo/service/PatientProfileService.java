package com.example.demo.service;

import com.example.demo.model.PatientProfile;
import java.util.List;
import java.util.Optional;

public interface PatientProfileService {
    PatientProfile createPatient(PatientProfile patientProfile);
    PatientProfile getPatientById(Long id);
    Optional<PatientProfile> findByPatientId(String patientId);
    Optional<PatientProfile> findByEmail(String email);
    List<PatientProfile> getAllPatients();
    List<PatientProfile> getActivePatients();
    List<PatientProfile> getPatientsByClinician(Long clinicianId);
    List<PatientProfile> getPatientsBySurgeryType(String surgeryType);
    List<PatientProfile> searchPatients(String keyword);
    PatientProfile updatePatient(Long id, PatientProfile updates);
    PatientProfile updatePatientStatus(Long id, Boolean active);
    void deletePatient(Long id);
    Long countActivePatients();
    List<PatientProfile> getPatientsWithRecentSurgeries(int days);
    PatientProfile assignClinician(Long patientId, Long clinicianId);
}