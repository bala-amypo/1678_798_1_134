package com.example.demo.repository;

import com.example.demo.model.RecoveryCurveProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecoveryCurveProfileRepository
        extends JpaRepository<RecoveryCurveProfile, Long> {

    List<RecoveryCurveProfile> findBySurgeryTypeOrderByDayNumberAsc(String surgeryType);
}
