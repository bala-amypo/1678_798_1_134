package com.example.demo.repository;

import com.example.demo.model.RecoveryCurveProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecoveryCurveProfileRepository
        extends JpaRepository<RecoveryCurveProfile, Long> {

    List<RecoveryCurveProfile>
        findBySurgeryTypeOrderByDayNumberAsc(String surgeryType);

    Optional<RecoveryCurveProfile>
        findBySurgeryTypeAndDayNumber(String surgeryType, Integer dayNumber);

    List<RecoveryCurveProfile> findBySurgeryType(String surgeryType);
}
