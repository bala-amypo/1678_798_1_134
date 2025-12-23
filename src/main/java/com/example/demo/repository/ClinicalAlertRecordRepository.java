package com.example.demo.repository;

import com.example.demo.model.ClinicalAlertRecord;

import java.util.List;
import java.util.Optional;

public interface ClinicalAlertRecordRepository {

    Optional<ClinicalAlertRecord> findById(Long id);

    List<ClinicalAlertRecord> findByPatientId(Long patientId);

    List<ClinicalAlertRecord> findAll();

    ClinicalAlertRecord save(ClinicalAlertRecord alert);
}
