package com.example.demo.repository;

import com.example.demo.model.DailySymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySymptomLogRepository extends JpaRepository<DailySymptomLog, Long> {
    
    // Find log for specific patient and date
    Optional<DailySymptomLog> findByPatientIdAndLogDate(Long patientId, LocalDate logDate);
    
    // Find all logs for a patient
    List<DailySymptomLog> findByPatientId(Long patientId);
    
    // Find logs for patient within date range
    List<DailySymptomLog> findByPatientIdAndLogDateBetween(Long patientId, LocalDate start, LocalDate end);
    
    // Find logs recorded by specific user
    List<DailySymptomLog> findByRecordedBy(Long userId);
    
    // Find logs with high pain levels
    @Query("SELECT l FROM DailySymptomLog l WHERE l.painLevel >= :threshold AND l.logDate >= :startDate")
    List<DailySymptomLog> findHighPainLogs(
            @Param("threshold") Integer threshold, 
            @Param("startDate") LocalDate startDate);
    
    // Find logs with low mobility levels
    @Query("SELECT l FROM DailySymptomLog l WHERE l.mobilityLevel <= :threshold AND l.logDate >= :startDate")
    List<DailySymptomLog> findLowMobilityLogs(
            @Param("threshold") Integer threshold, 
            @Param("startDate") LocalDate startDate);
    
    // Check if log exists for patient and date
    boolean existsByPatientIdAndLogDate(Long patientId, LocalDate logDate);
    
    // Find today's logs for a patient
    @Query("SELECT l FROM DailySymptomLog l WHERE l.patientId = :patientId AND l.logDate = CURRENT_DATE")
    Optional<DailySymptomLog> findTodaysLogByPatient(@Param("patientId") Long patientId);
    
    // Find latest logs for a patient (with pagination)
    @Query("SELECT l FROM DailySymptomLog l WHERE l.patientId = :patientId ORDER BY l.logDate DESC")
    List<DailySymptomLog> findLatestLogsByPatient(
            @Param("patientId") Long patientId,
            org.springframework.data.domain.Pageable pageable);
    
    // Find logs where medication was taken
    List<DailySymptomLog> findByMedicationTakenTrueAndLogDateBetween(
            LocalDate startDate, 
            LocalDate endDate);
    
    // Find logs where therapy was completed
    List<DailySymptomLog> findByTherapyCompletedTrueAndLogDateBetween(
            LocalDate startDate, 
            LocalDate endDate);
    
    // Calculate average pain level for patient
    @Query("SELECT AVG(l.painLevel) FROM DailySymptomLog l WHERE l.patientId = :patientId AND l.logDate BETWEEN :startDate AND :endDate")
    Double calculateAveragePainLevel(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    // Calculate average mobility level for patient
    @Query("SELECT AVG(l.mobilityLevel) FROM DailySymptomLog l WHERE l.patientId = :patientId AND l.logDate BETWEEN :startDate AND :endDate")
    Double calculateAverageMobilityLevel(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    // Find logs with wellness score below threshold
    @Query("SELECT l FROM DailySymptomLog l WHERE (10 - l.painLevel) * 0.4 + l.mobilityLevel * 0.4 + (10 - l.fatigueLevel) * 0.2 < :threshold")
    List<DailySymptomLog> findLowWellnessScoreLogs(@Param("threshold") Double threshold);
    
    // Count logs per patient
    @Query("SELECT l.patientId, COUNT(l) FROM DailySymptomLog l GROUP BY l.patientId")
    List<Object[]> countLogsPerPatient();
    
    // Find missing log dates for a patient
    @Query(value = """
        WITH date_series AS (
            SELECT generate_series(
                CAST(:startDate AS DATE), 
                CAST(:endDate AS DATE), 
                '1 day'::interval
            )::DATE AS log_date
        )
        SELECT ds.log_date 
        FROM date_series ds
        LEFT JOIN daily_symptom_logs l ON ds.log_date = l.log_date AND l.patient_id = :patientId
        WHERE l.id IS NULL
        ORDER BY ds.log_date
        """, nativeQuery = true)
    List<LocalDate> findMissingLogDates(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}