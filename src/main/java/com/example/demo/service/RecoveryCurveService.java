package com.example.demo.service;

import com.example.demo.model.RecoveryCurveProfile;

import java.util.List;
import java.util.Optional;

public interface RecoveryCurveService {
    RecoveryCurveProfile createCurveEntry(RecoveryCurveProfile entry);
    RecoveryCurveProfile createProfile(RecoveryCurveProfile profile);
    List<RecoveryCurveProfile> getCurveForSurgery(String surgeryType);
    List<RecoveryCurveProfile> getProfilesBySurgeryType(String surgeryType);
    List<RecoveryCurveProfile> getAllCurves();
    List<RecoveryCurveProfile> getAllProfiles();
    Optional<RecoveryCurveProfile> getCurveByDayAndSurgery(String surgeryType, Integer dayNumber);
    Optional<RecoveryCurveProfile> getCurveById(Long id);
}