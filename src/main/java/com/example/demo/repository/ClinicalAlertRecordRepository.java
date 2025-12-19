package com.example.demo.repository;

import com.example.demo.model.ClinicalAlertRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicalAlertRecordRepository
        extends JpaRepository<ClinicalAlertRecord, Long> {

    List<ClinicalAlertRecord> findByPatientId(Long patientId);
}
