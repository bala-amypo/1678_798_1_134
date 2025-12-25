package com.example.demo.service.impl;

import com.example.demo.model.RecoveryCurveProfile;
import com.example.demo.repository.RecoveryCurveProfileRepository;
import com.example.demo.service.RecoveryCurveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecoveryCurveServiceImpl implements RecoveryCurveService {

    private final RecoveryCurveProfileRepository recoveryCurveProfileRepository;

    public RecoveryCurveServiceImpl(RecoveryCurveProfileRepository recoveryCurveProfileRepository) {
        this.recoveryCurveProfileRepository = recoveryCurveProfileRepository;
    }

    @Override
    public RecoveryCurveProfile createCurveEntry(RecoveryCurveProfile curve) {
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
}