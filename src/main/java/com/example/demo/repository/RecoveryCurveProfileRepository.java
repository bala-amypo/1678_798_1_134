package com.example.demo.repository;

import com.example.demo.model.RecoveryCurveProfile;

import java.util.List;

public interface RecoveryCurveProfileRepository {

    List<RecoveryCurveProfile> findAll();

    List<RecoveryCurveProfile> findBySurgeryTypeOrderByDayNumberAsc(String surgeryType);

    RecoveryCurveProfile save(RecoveryCurveProfile profile);
}
