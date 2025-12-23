package com.example.demo.repository;

import com.example.demo.model.ClinicalAlertRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalAlertRecordRepository
        extends JpaRepository<ClinicalAlertRecord, Long> {
}
