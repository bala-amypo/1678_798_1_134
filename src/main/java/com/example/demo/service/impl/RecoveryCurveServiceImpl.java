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
        if (curve.getMinFatigueLevel() != null) {
            existing.setMinFatigueLevel(curve.getMinFatigueLevel());
        }
        if (curve.getMaxFatigueLevel() != null) {
            existing.setMaxFatigueLevel(curve.getMaxFatigueLevel());
        }
        if (curve.getRecoveryPhase() != null) {
            existing.setRecoveryPhase(curve.getRecoveryPhase());
        }
        if (curve.getRecommendations() != null) {
            existing.setRecommendations(curve.getRecommendations());
        }
        if (curve.getExpectedActivities() != null) {
            existing.setExpectedActivities(curve.getExpectedActivities());
        }
        if (curve.getWarningSigns() != null) {
            existing.setWarningSigns(curve.getWarningSigns());
        }
        
        RecoveryCurveProfile updated = recoveryCurveProfileRepository.save(existing);
        log.info("Recovery curve entry updated successfully");
        
        return updated;
    }
    
    @Override
    public void deleteCurveEntry(Long id) {
        log.info("Deleting recovery curve entry with ID: {}", id);
        
        RecoveryCurveProfile curve = recoveryCurveProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recovery curve", "id", id));
        
        recoveryCurveProfileRepository.delete(curve);
        log.info("Recovery curve entry deleted successfully");
    }
    
    @Override
    public List<String> getAllSurgeryTypes() {
        log.debug("Fetching all surgery types");
        return recoveryCurveProfileRepository.findDistinctSurgeryTypes();
    }
    
    @Override
    public Optional<Integer> getMaxDayForSurgeryType(String surgeryType) {
        log.debug("Fetching max day number for surgery type: {}", surgeryType);
        return recoveryCurveProfileRepository.findMaxDayNumberBySurgeryType(surgeryType);
    }
    
    @Override
    public Map<String, Object> calculateExpectedValues(String surgeryType, Integer dayNumber) {
        log.debug("Calculating expected values for surgery: {}, day: {}", surgeryType, dayNumber);
        
        Optional<RecoveryCurveProfile> curve = getCurveEntry(surgeryType, dayNumber);
        
        if (curve.isEmpty()) {
            return Map.of(
                "found", false,
                "message", String.format("No recovery curve found for surgery %s, day %d", surgeryType, dayNumber)
            );
        }
        
        RecoveryCurveProfile rc = curve.get();
        
        return Map.of(
            "found", true,
            "surgeryType", rc.getSurgeryType(),
            "dayNumber", rc.getDayNumber(),
            "expectedPainLevel", rc.getExpectedPainLevel(),
            "expectedMobilityLevel", rc.getExpectedMobilityLevel(),
            "expectedFatigueLevel", rc.getExpectedFatigueLevel(),
            "recoveryPhase", rc.getRecoveryPhase(),
            "wellnessScore", rc.getExpectedWellnessScore()
        );
    }
    
    @Override
    public boolean validateActualValues(String surgeryType, Integer dayNumber, 
                                       Integer painLevel, Integer mobilityLevel, Integer fatigueLevel) {
        log.debug("Validating actual values for surgery: {}, day: {}", surgeryType, dayNumber);
        
        Optional<RecoveryCurveProfile> curve = getCurveEntry(surgeryType, dayNumber);
        
        if (curve.isEmpty()) {
            log.warn("No recovery curve found for validation");
            return false;
        }
        
        RecoveryCurveProfile rc = curve.get();
        
        boolean painValid = rc.isPainWithinRange(painLevel);
        boolean mobilityValid = rc.isMobilityWithinRange(mobilityLevel);
        boolean fatigueValid = rc.isFatigueWithinRange(fatigueLevel);
        
        log.debug("Validation results - Pain: {}, Mobility: {}, Fatigue: {}", 
                painValid, mobilityValid, fatigueValid);
        
        return painValid && mobilityValid && fatigueValid;
    }
    
    @Override
    public List<RecoveryCurveProfile> getCurvesByDayRange(String surgeryType, Integer minDay, Integer maxDay) {
        log.debug("Fetching curves for surgery: {}, days {}-{}", surgeryType, minDay, maxDay);
        return recoveryCurveProfileRepository.findBySurgeryTypeAndDayRange(surgeryType, minDay, maxDay);
    }
    
    // Helper method to get curve for specific patient day
    public Optional<RecoveryCurveProfile> getCurveForPatientDay(Long patientId, LocalDate logDate) {
        // This would need patient repository to get surgery date
        // For now, returning empty
        return Optional.empty();
    }
    
    // Helper method to generate recovery report
    public Map<String, Object> generateRecoveryReport(String surgeryType) {
        log.debug("Generating recovery report for surgery type: {}", surgeryType);
        
        List<RecoveryCurveProfile> curves = getCurveForSurgery(surgeryType);
        
        if (curves.isEmpty()) {
            return Map.of("error", "No recovery curves found for surgery type: " + surgeryType);
        }
        
        Map<String, Object> report = new HashMap<>();
        report.put("surgeryType", surgeryType);
        report.put("totalDays", curves.size());
        report.put("phases", new HashMap<>());
        
        // Group by phase
        curves.forEach(curve -> {
            String phase = curve.getRecoveryPhase() != null ? curve.getRecoveryPhase() : "UNKNOWN";
            ((Map<String, List<RecoveryCurveProfile>>) report.get("phases"))
                .computeIfAbsent(phase, k -> new ArrayList<>())
                .add(curve);
        });
        
        // Calculate averages
        double avgPain = curves.stream().mapToInt(RecoveryCurveProfile::getExpectedPainLevel).average().orElse(0);
        double avgMobility = curves.stream().mapToInt(RecoveryCurveProfile::getExpectedMobilityLevel).average().orElse(0);
        double avgFatigue = curves.stream().mapToInt(RecoveryCurveProfile::getExpectedFatigueLevel).average().orElse(0);
        
        report.put("averagePainLevel", avgPain);
        report.put("averageMobilityLevel", avgMobility);
        report.put("averageFatigueLevel", avgFatigue);
        
        return report;
    }
}