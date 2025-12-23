package com.example.demo.repository;

import com.example.demo.model.DeviationRule;

import java.util.List;
import java.util.Optional;

public interface DeviationRuleRepository {

    Optional<DeviationRule> findByRuleCode(String ruleCode);

    List<DeviationRule> findBySurgeryType(String surgeryType); // ✅ FIX

    List<DeviationRule> findByActiveTrue();

    Optional<DeviationRule> findById(Long id);

    List<DeviationRule> findAll();

    DeviationRule save(DeviationRule rule);
}
