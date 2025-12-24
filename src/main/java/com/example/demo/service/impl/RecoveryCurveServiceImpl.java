package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.RecoveryCurveProfile;
import com.example.demo.repository.RecoveryCurveProfileRepository;
import com.example.demo.service.RecoveryCurveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecoveryCurveServiceImpl implements RecoveryCurveService {
    
    private final RecoveryCurveProfileRepository recoveryCurveProfileRepository;
    
    @Override
    @Transactional
    public RecoveryCurveProfile createCurveEntry(RecoveryCurveProfile curve) {
        // Validate required fields
        if (curve.getSurgeryType() == null || curve.getSurgeryType().trim().isEmpty()) {
            throw new IllegalArgumentException("Surgery type is required");
        }
        
        if (curve.getDayNumber() == null || curve.getDayNumber() < 0) {
            throw new IllegalArgumentException("Day number must be non-negative");
        }
        
        // Check for duplicate entry for same surgery type and day
        List<RecoveryCurveProfile> existing = recoveryCurveProfileRepository
                .findBySurgeryTypeOrderByDayNumberAsc(curve.getSurgeryType());
        
        boolean duplicate = existing.stream()
                .anyMatch(c -> c.getDayNumber().equals(curve.getDayNumber()));
        
        if (duplicate) {
            throw new IllegalArgumentException(
                    String.format("Curve entry already exists for surgery type %s on day %d",
                            curve.getSurgeryType(), curve.getDayNumber()));
        }
        
        // Validate symptom levels
        validateSymptomLevel(curve.getExpectedPainLevel(), "Pain");
        validateSymptomLevel(curve.getExpectedMobilityLevel(), "Mobility");
        validateSymptomLevel(curve.getExpectedFatigueLevel(), "Fatigue");
        
        return recoveryCurveProfileRepository.save(curve);
    }
    
    @Override
    public List<RecoveryCurveProfile> getCurveForSurgery(String surgeryType) {
        return recoveryCurveProfileRepository.findBySurgeryTypeOrderByDayNumberAsc(surgeryType);
    }
    
    @Override
    public List<RecoveryCurveProfile> getAllCurves() {
        return recoveryCurveProfileRepository.findAll();
    }
    
    @Override
    public RecoveryCurveProfile getCurveById(Long id) {
        return recoveryCurveProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recovery curve not found with id: " + id));
    }
    
    @Override
    @Transactional
    public RecoveryCurveProfile updateCurve(Long id, RecoveryCurveProfile curve) {
        RecoveryCurveProfile existing = getCurveById(id);
        
        // Validate symptom levels
        validateSymptomLevel(curve.getExpectedPainLevel(), "Pain");
        validateSymptomLevel(curve.getExpectedMobilityLevel(), "Mobility");
        validateSymptomLevel(curve.getExpectedFatigueLevel(), "Fatigue");
        
        existing.setExpectedPainLevel(curve.getExpectedPainLevel());
        existing.setExpectedMobilityLevel(curve.getExpectedMobilityLevel());
        existing.setExpectedFatigueLevel(curve.getExpectedFatigueLevel());
        
        return recoveryCurveProfileRepository.save(existing);
    }
    
    @Override
    @Transactional
    public void deleteCurve(Long id) {
        RecoveryCurveProfile curve = getCurveById(id);
        recoveryCurveProfileRepository.delete(curve);
    }
    
    private void validateSymptomLevel(Integer level, String symptomName) {
        if (level != null && (level < 0 || level > 10)) {
            throw new IllegalArgumentException(symptomName + " level must be between 0 and 10");
        }
    }
}