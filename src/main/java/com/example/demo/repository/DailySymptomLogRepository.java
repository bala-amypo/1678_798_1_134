package com.example.demo.repository;

import com.example.demo.model.DailySymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailySymptomLogRepository extends JpaRepository<DailySymptomLog, Long> {
    List<DailySymptomLog> findByPatientId(Long patientId);
    Optional<DailySymptomLog> findByPatientIdAndLogDate(Long patientId, LocalDate logDate);
}