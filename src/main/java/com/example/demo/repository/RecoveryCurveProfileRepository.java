package com.example.demo.repository;

import com.example.demo.model.RecoveryCurveProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecoveryCurveProfileRepository
        extends JpaRepository<RecoveryCurveProfile, Long> {

    List<RecoveryCurveProfile> findBySurgeryTypeOrderByDayNumberAsc(String surgeryType);
}
