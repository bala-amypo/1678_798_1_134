package com.example.demo.service;

import com.example.demo.model.RecoveryCurveProfile;

import java.util.List;
import java.util.Optional;

public interface RecoveryCurveService {

    RecoveryCurveProfile createCurveEntry(RecoveryCurveProfile entry);

    List<RecoveryCurveProfile> getCurveForSurgery(String surgeryType);

    List<RecoveryCurveProfile> getAllCurves();

    Optional<RecoveryCurveProfile> getCurveByDayAndSurgery(
            String surgeryType, Integer dayNumber);
}
