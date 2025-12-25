package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.repository.ClinicalAlertRecordRepository;
import com.example.demo.service.ClinicalAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalAlertServiceImpl implements ClinicalAlertService {

    private final ClinicalAlertRecordRepository clinicalAlertRecordRepository;

    public ClinicalAlertServiceImpl(ClinicalAlertRecordRepository clinicalAlertRecordRepository) {
        this.clinicalAlertRecordRepository = clinicalAlertRecordRepository;
    }

    @Override
    public ClinicalAlertRecord resolveAlert(Long id) {
        ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
        alert.setResolved(true);
        return clinicalAlertRecordRepository.save(alert);
    }

    @Override
    public List<ClinicalAlertRecord> getAlertsByPatient(Long patientId) {
        return clinicalAlertRecordRepository.findByPatientId(patientId);
    }

    @Override
    public Optional<ClinicalAlertRecord> getAlertById(Long id) {
        return clinicalAlertRecordRepository.findById(id);
    }

    @Override
    public List<ClinicalAlertRecord> getAllAlerts() {
        return clinicalAlertRecordRepository.findAll();
    }
}