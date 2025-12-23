package com.example.demo.repository;

import com.example.demo.model.DailySymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailySymptomLogRepository
        extends JpaRepository<DailySymptomLog, Long> {

    List<DailySymptomLog> findByPatientId(Long patientId);
}
