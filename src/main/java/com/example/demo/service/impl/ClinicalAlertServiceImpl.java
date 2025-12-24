package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.repository.ClinicalAlertRecordRepository;
import com.example.demo.service.ClinicalAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicalAlertServiceImpl implements ClinicalAlertService {
    
    private final ClinicalAlertRecordRepository clinicalAlertRecordRepository;
    
    @Override
    @Transactional
    public ClinicalAlertRecord createAlert(ClinicalAlertRecord alert) {
        // Validate required fields
        if (alert.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        
        if (alert.getAlertType() == null || alert.getAlertType().trim().isEmpty()) {
            throw new IllegalArgumentException("Alert type is required");
        }
        
        if (alert.getSeverity() == null || alert.getSeverity().trim().isEmpty()) {
            throw new IllegalArgumentException("Severity is required");
        }
        
        // Set default resolved status if not provided
        if (alert.getResolved() == null) {
            alert.setResolved(false);
        }
        
        return clinicalAlertRecordRepository.save(alert);
    }
    
    @Override
    public List<ClinicalAlertRecord> getAllAlerts() {
        return clinicalAlertRecordRepository.findAll();
    }
    
    @Override
    public Optional<ClinicalAlertRecord> getAlertById(Long id) {
        return clinicalAlertRecordRepository.findById(id);
    }
    
    @Override
    public List<ClinicalAlertRecord> getAlertsByPatient(Long patientId) {
        return clinicalAlertRecordRepository.findByPatientId(patientId);
    }
    
    @Override
    @Transactional
    public ClinicalAlertRecord resolveAlert(Long id) {
        ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical alert not found with id: " + id));
        
        alert.setResolved(true);
        
        return clinicalAlertRecordRepository.save(alert);
    }
    
    @Override
    public List<ClinicalAlertRecord> getUnresolvedAlerts() {
        return clinicalAlertRecordRepository.findByResolvedFalse();
    }
    
    @Override
    @Transactional
    public void deleteAlert(Long id) {
        ClinicalAlertRecord alert = clinicalAlertRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical alert not found with id: " + id));
        clinicalAlertRecordRepository.delete(alert);
    }
}