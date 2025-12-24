package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.repository.ClinicalAlertRecordRepository;
import com.example.demo.service.ClinicalAlertService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service   // 🔴 THIS WAS MISSING
public class ClinicalAlertServiceImpl implements ClinicalAlertService {

    private final ClinicalAlertRecordRepository repository;

    // Constructor injection (REQUIRED)
    public ClinicalAlertServiceImpl(ClinicalAlertRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClinicalAlertRecord createAlert(ClinicalAlertRecord alert) {
        return repository.save(alert);
    }

    @Override
    public List<ClinicalAlertRecord> getAlertsByPatient(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    @Override
    public Optional<ClinicalAlertRecord> getAlertById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ClinicalAlertRecord resolveAlert(Long alertId) {
        ClinicalAlertRecord alert = repository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
        alert.setResolved(true);
        return repository.save(alert);
    }

    @Override
    public List<ClinicalAlertRecord> getAllAlerts() {
        return repository.findAll();
    }
}
