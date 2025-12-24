package com.example.demo.service;

import com.example.demo.model.RecoveryCurveProfile;
import java.util.List;

public interface RecoveryCurveService {
    RecoveryCurveProfile createCurveEntry(RecoveryCurveProfile curve);
    List<RecoveryCurveProfile> getCurveForSurgery(String surgeryType);
    List<RecoveryCurveProfile> getAllCurves();
    RecoveryCurveProfile getCurveById(Long id);
    RecoveryCurveProfile updateCurve(Long id, RecoveryCurveProfile curve);
    void deleteCurve(Long id);
}