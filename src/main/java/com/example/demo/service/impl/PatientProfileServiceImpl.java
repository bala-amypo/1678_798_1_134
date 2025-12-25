package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PatientProfile;
import com.example.demo.repository.PatientProfileRepository;
import com.example.demo.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PatientProfileServiceImpl implements PatientProfileService {
    
    private final PatientProfileRepository patientProfileRepository;
    
    @Override
    public PatientProfile createPatient(PatientProfile patientProfile) {
        log.info("Creating new patient profile");
        
        // Generate patient ID if not provided
        if (patientProfile.getPatientId() == null) {
            String patientId = "PAT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            patientProfile.setPatientId(patientId);
        }
        
        // Set default values
        if (patientProfile.getActive() == null) {
            patientProfile.setActive(true);
        }
        if (patientProfile.getSurgeryDate() == null) {
            patientProfile.setSurgeryDate(LocalDateTime.now());
        }
        
        PatientProfile savedPatient = patientProfileRepository.save(patientProfile);
        log.info("Patient created with ID: {}", savedPatient.getPatientId());
        
        return savedPatient;
    }
    
    @Override
    public PatientProfile getPatientById(Long id) {
        log.debug("Fetching patient by ID: {}", id);
        return patientProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
    }
    
    @Override
    public Optional<PatientProfile> findByPatientId(String patientId) {
        log.debug("Finding patient by patient ID: {}", patientId);
        return patientProfileRepository.findByPatientId(patientId);
    }
    
    @Override
    public Optional<PatientProfile> findByEmail(String email) {
        log.debug("Finding patient by email: {}", email);
        return patientProfileRepository.findByEmail(email);
    }
    
    @Override
    public List<PatientProfile> getAllPatients() {
        log.debug("Fetching all patients");
        return patientProfileRepository.findAll();
    }
    
    @Override
    public List<PatientProfile> getActivePatients() {
        log.debug("Fetching active patients");
        return patientProfileRepository.findByActiveTrue();
    }
    
    @Override
    public List<PatientProfile> getPatientsByClinician(Long clinicianId) {
        log.debug("Fetching patients for clinician ID: {}", clinicianId);
        return patientProfileRepository.findByAttendingClinicianId(clinicianId);
    }
    
    @Override
    public List<PatientProfile> getPatientsBySurgeryType(String surgeryType) {
        log.debug("Fetching patients with surgery type: {}", surgeryType);
        return patientProfileRepository.findBySurgeryTypeAndActiveTrue(surgeryType);
    }
    
    @Override
    public List<PatientProfile> searchPatients(String keyword) {
        log.debug("Searching patients with keyword: {}", keyword);
        return patientProfileRepository.searchByFullName(keyword);
    }
    
    @Override
    public PatientProfile updatePatient(Long id, PatientProfile updates) {
        log.info("Updating patient with ID: {}", id);
        
        PatientProfile existing = getPatientById(id);
        
        if (updates.getFullName() != null) {
            existing.setFullName(updates.getFullName());
        }
        if (updates.getAge() != null) {
            existing.setAge(updates.getAge());
        }
        if (updates.getGender() != null) {
            existing.setGender(updates.getGender());
        }
        if (updates.getEmail() != null) {
            existing.setEmail(updates.getEmail());
        }
        if (updates.getPhoneNumber() != null) {
            existing.setPhoneNumber(updates.getPhoneNumber());
        }
        if (updates.getSurgeryType() != null) {
            existing.setSurgeryType(updates.getSurgeryType());
        }
        if (updates.getSurgeryDate() != null) {
            existing.setSurgeryDate(updates.getSurgeryDate());
        }
        if (updates.getMedicalHistory() != null) {
            existing.setMedicalHistory(updates.getMedicalHistory());
        }
        if (updates.getEmergencyContactName() != null) {
            existing.setEmergencyContactName(updates.getEmergencyContactName());
        }
        if (updates.getEmergencyContactPhone() != null) {
            existing.setEmergencyContactPhone(updates.getEmergencyContactPhone());
        }
        if (updates.getAddress() != null) {
            existing.setAddress(updates.getAddress());
        }
        if (updates.getAttendingClinicianId() != null) {
            existing.setAttendingClinicianId(updates.getAttendingClinicianId());
        }
        if (updates.getActive() != null) {
            existing.setActive(updates.getActive());
        }
        
        PatientProfile updated = patientProfileRepository.save(existing);
        log.info("Patient updated successfully: {}", updated.getPatientId());
        
        return updated;
    }
    
    @Override
    public PatientProfile updatePatientStatus(Long id, Boolean active) {
        log.info("Updating patient status. ID: {}, Active: {}", id, active);
        
        PatientProfile patient = getPatientById(id);
        patient.setActive(active);
        
        PatientProfile updated = patientProfileRepository.save(patient);
        log.info("Patient status updated: {} is now {}", updated.getPatientId(), 
                active ? "active" : "inactive");
        
        return updated;
    }
    
    @Override
    public void deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);
        
        PatientProfile patient = getPatientById(id);
        patientProfileRepository.delete(patient);
        
        log.info("Patient deleted: {}", patient.getPatientId());
    }
    
    @Override
    public Long countActivePatients() {
        return patientProfileRepository.findByActiveTrue().stream().count();
    }
    
    @Override
    public List<PatientProfile> getPatientsWithRecentSurgeries(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return patientProfileRepository.findPatientsWithRecentSurgeries(cutoffDate);
    }
    
    @Override
    public PatientProfile assignClinician(Long patientId, Long clinicianId) {
        log.info("Assigning clinician {} to patient {}", clinicianId, patientId);
        
        PatientProfile patient = getPatientById(patientId);
        patient.setAttendingClinicianId(clinicianId);
        
        PatientProfile updated = patientProfileRepository.save(patient);
        log.info("Clinician assigned successfully");
        
        return updated;
    }
}