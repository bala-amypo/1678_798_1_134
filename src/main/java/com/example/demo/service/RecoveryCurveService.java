package com.example.demo.service;

import com.example.demo.model.RecoveryCurveProfile;
import java.util.List;
import java.util.Optional;

public interface RecoveryCurveService {
    RecoveryCurveProfile createCurveEntry(RecoveryCurveProfile curve);
    List<RecoveryCurveProfile> getAllCurves();
    List<RecoveryCurveProfile> getCurveForSurgery(String surgeryType);
    Optional<RecoveryCurveProfile> getCurveEntry(String surgeryType, Integer dayNumber);
    List<RecoveryCurveProfile> getCurveForPhase(String surgeryType, String phase);
    RecoveryCurveProfile updateCurveEntry(Long id, RecoveryCurveProfile curve);
    void deleteCurveEntry(Long id);
    List<String> getAllSurgeryTypes();
    Optional<Integer> getMaxDayForSurgeryType(String surgeryType);
    Map<String, Object> calculateExpectedValues(String surgeryType, Integer dayNumber);
    boolean validateActualValues(String surgeryType, Integer dayNumber, 
                                 Integer painLevel, Integer mobilityLevel, Integer fatigueLevel);
    List<RecoveryCurveProfile> getCurvesByDayRange(String surgeryType, Integer minDay, Integer maxDay);
}