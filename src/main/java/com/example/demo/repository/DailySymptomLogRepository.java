package com.example.demo.repository;

import com.example.demo.model.DailySymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DailySymptomLogRepository
        extends JpaRepository<DailySymptomLog, Long> {
    List<DailySymptomLog> findByPatientid(Long patientid);
}
