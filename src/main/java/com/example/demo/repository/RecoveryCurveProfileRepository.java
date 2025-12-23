package com.example.demo.repository;

import com.example.demo.model.RecoveryCurveProfile;

import java.util.List;
import java.util.Optional;

public interface RecoveryCurveProfileRepository {

    List<RecoveryCurveProfile> findBySurgeryType(String surgeryType);

    Optional<RecoveryCurveProfile> findBySurgeryTypeAndDayNumber(
            String surgeryType,
            Integer dayNumber
    );

    Optional<RecoveryCurveProfile> findById(Long id);

    List<RecoveryCurveProfile> findAll();

    RecoveryCurveProfile save(RecoveryCurveProfile profile);
}
