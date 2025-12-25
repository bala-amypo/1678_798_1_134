package com.example.demo.repository;

import com.example.demo.model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    
    // Find by patient ID
    Optional<PatientProfile> findByPatientId(String patientId);
    
    // Find by email
    Optional<PatientProfile> findByEmail(String email);
    
    // Find by attending clinician
    List<PatientProfile> findByAttendingClinicianId(Long clinicianId);
    
    // Find active patients
    List<PatientProfile> findByActiveTrue();
    
    // Find patients by surgery type
    List<PatientProfile> findBySurgeryType(String surgeryType);
    
    // Find active patients by surgery type
    List<PatientProfile> findBySurgeryTypeAndActiveTrue(String surgeryType);
    
    // Find patients by age range
    @Query("SELECT p FROM PatientProfile p WHERE p.age >= :minAge AND p.age <= :maxAge AND p.active = true")
    List<PatientProfile> findByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    // Find patients by gender
    List<PatientProfile> findByGenderAndActiveTrue(String gender);
    
    // Find patients with upcoming surgeries
    @Query("SELECT p FROM PatientProfile p WHERE p.surgeryDate >= :startDate AND p.surgeryDate <= :endDate AND p.active = true")
    List<PatientProfile> findPatientsWithSurgeriesBetween(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);
    
    // Find patients who had surgery recently
    @Query("SELECT p FROM PatientProfile p WHERE p.surgeryDate >= :startDate AND p.active = true")
    List<PatientProfile> findPatientsWithRecentSurgeries(@Param("startDate") LocalDateTime startDate);
    
    // Search patients by name
    @Query("SELECT p FROM PatientProfile p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%')) AND p.active = true")
    List<PatientProfile> searchByFullName(@Param("name") String name);
    
    // Count patients by surgery type
    @Query("SELECT p.surgeryType, COUNT(p) FROM PatientProfile p WHERE p.active = true GROUP BY p.surgeryType")
    List<Object[]> countPatientsBySurgeryType();
    
    // Find patients without attending clinician
    @Query("SELECT p FROM PatientProfile p WHERE p.attendingClinicianId IS NULL AND p.active = true")
    List<PatientProfile> findPatientsWithoutClinician();
    
    // Find patients created after specific date
    List<PatientProfile> findByCreatedAtAfter(LocalDateTime date);
    
    // Find patients by emergency contact phone
    List<PatientProfile> findByEmergencyContactPhone(String phone);
    
    // Find patients with specific medical history keyword
    @Query("SELECT p FROM PatientProfile p WHERE LOWER(p.medicalHistory) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.active = true")
    List<PatientProfile> findByMedicalHistoryKeyword(@Param("keyword") String keyword);
    
    // Find patients by location/address
    @Query("SELECT p FROM PatientProfile p WHERE LOWER(p.address) LIKE LOWER(CONCAT('%', :location, '%')) AND p.active = true")
    List<PatientProfile> findByAddressContaining(@Param("location") String location);
}