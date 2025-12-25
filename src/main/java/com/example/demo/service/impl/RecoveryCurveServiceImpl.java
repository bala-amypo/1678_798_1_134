package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.RecoveryCurveProfile;
import com.example.demo.repository.RecoveryCurveProfileRepository;
import com.example.demo.service.RecoveryCurveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecoveryCurveServiceImpl implements RecoveryCurveService {
    
    private final RecoveryCurveProfileRepository recoveryCurveProfileRepository;
    
    @Override
    public RecoveryCurveProfile createCurveEntry(RecoveryCurveProfile curve) {
        log.info("Creating recovery curve entry for surgery: {}, day: {}", 
                curve.getSurgeryType(), curve.getDayNumber());
        
        // Check if entry already exists
        Optional<RecoveryCurveProfile> existing = recoveryCurveProfileRepository
                .findBySurgeryTypeAndDayNumber(curve.getSurgeryType(), curve.getDayNumber());
        
        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Recovery curve already exists for surgery %s, day %d", 
                            curve.getSurgeryType(), curve.getDayNumber()));
        }
        
        RecoveryCurveProfile savedCurve = recoveryCurveProfileRepository.save(curve);
        log.info("Recovery curve entry created with ID: {}", savedCurve.getId());
        
        return savedCurve;
    }
    
    @Override
    public List<RecoveryCurveProfile> getAllCurves() {
        log.debug("Fetching all recovery curves");
        return recoveryCurveProfileRepository.findAll();
    }
    
    @Override
    public List<RecoveryCurveProfile> getCurveForSurgery(String surgeryType) {
        log.debug("Fetching recovery curve for surgery type: {}", surgeryType);
        return recoveryCurveProfileRepository.findBySurgeryTypeOrderByDayNumberAsc(surgeryType);
    }
    
    @Override
    public Optional<RecoveryCurveProfile> getCurveEntry(String surgeryType, Integer dayNumber) {
        log.debug("Fetching recovery curve for surgery: {}, day: {}", surgeryType, dayNumber);
        return recoveryCurveProfileRepository.findBySurgeryTypeAndDayNumber(surgeryType, dayNumber);
    }
    
    @Override
    public List<RecoveryCurveProfile> getCurveForPhase(String surgeryType, String phase) {
        log.debug("Fetching recovery curve for surgery: {}, phase: {}", surgeryType, phase);
        return recoveryCurveProfileRepository.findBySurgeryTypeAndRecoveryPhase(surgeryType, phase);
    }
    
    @Override
    public RecoveryCurveProfile updateCurveEntry(Long id, RecoveryCurveProfile curve) {
        log.info("Updating recovery curve entry with ID: {}", id);
        
        RecoveryCurveProfile existing = recoveryCurveProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recovery curve", "id", id));
        
        // Update fields if provided
        if (curve.getSurgeryType() != null) {
            existing.setSurgeryType(curve.getSurgeryType());
        }
        if (curve.getDayNumber() != null) {
            existing.setDayNumber(curve.getDayNumber());
        }
        if (curve.getExpectedPainLevel() != null) {
            existing.setExpectedPainLevel(curve.getExpectedPainLevel());
        }
        if (curve.getExpectedMobilityLevel() != null) {
            existing.setExpectedMobilityLevel(curve.getExpectedMobilityLevel());
        }
        if (curve.getExpectedFatigueLevel() != null) {
            existing.setExpectedFatigueLevel(curve.getExpectedFatigueLevel());
        }
        if (curve.getMinPainLevel() != null) {
            existing.setMinPainLevel(curve.getMinPainLevel());
        }
        if (curve.getMaxPainLevel() != null) {
            existing.setMaxPainLevel(curve.getMaxPainLevel());
        }
        if (curve.getMinMobilityLevel() != null) {
            existing.setMinMobilityLevel(curve.getMinMobilityLevel());
        }
        if (curve.getMaxMobilityLevel() != null) {
            existing.setMaxMobilityLevel(curve.getMaxMobilityLevel());
        }
        if (curve.getMinFatigueLevel