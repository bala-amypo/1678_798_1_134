package com.example.demo.repository;

import com.example.demo.model.ClinicalAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClinicalAlertRepository
        extends JpaRepository<ClinicalAlert, Long> {
    List<ClinicalAlert> findByPatientid(Long patientid);
}
