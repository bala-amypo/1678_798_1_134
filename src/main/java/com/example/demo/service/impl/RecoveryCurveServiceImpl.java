package com.example.demo.service.impl;

import com.example.demo.model.RecoveryCurveProfile;
import com.example.demo.repository.RecoveryCurveProfileRepository;
import com.example.demo.service.RecoveryCurveService;

import java.util.List;
import java.util.Optional;

public class RecoveryCurveServiceImpl implements RecoveryCurveService {

    private final RecoveryCurveProfileRepository repository;

    public RecoveryCurveServiceImpl(
            RecoveryCurveProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public RecoveryCurveProfile createCurveEntry(
            RecoveryCurveProfile entry) {
        return repository.save(entry);
    }

    @Override
    public List<RecoveryCurveProfile> getCurveForSurgery(
            String surgeryType) {
        return repository
                .findBySurgeryTypeOrderByDayNumberAsc(surgeryType);
    }

    @Override
    public List<RecoveryCurveProfile> getAllCurves() {
        return repository.findAll();
    }

    @Override
    public Optional<RecoveryCurveProfile> getCurveByDayAndSurgery(
            String surgeryType,
            Integer dayNumber) {

        return repository
                .findBySurgeryTypeAndDayNumber(surgeryType, dayNumber);
    }
}
