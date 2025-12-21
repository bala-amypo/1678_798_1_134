package com.example.demo.repository;

import com.example.demo.model.DailySymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySymptomLogRepository extends JpaRepository<DailySymptomLog, Long> {
    List<DailySymptomLog> findByPatientId(Long patientId);
    Optional<DailySymptomLog> findByPatientIdAndLogDate(Long patientId, LocalDate logDate);
    List<DailySymptomLog> findByLogDateBetween(LocalDate startDate, LocalDate endDate);
}