package com.example.demo.repository;

import com.example.demo.model.DeviationRule;

import java.util.List;
import java.util.Optional;

/**
 * Plain repository abstraction
 * NO Spring Data
 * NO JPA
 */
public interface DeviationRuleRepository {

    Optional<DeviationRule> findByRuleCode(String ruleCode);

    List<DeviationRule> findByActiveTrue();

    Optional<DeviationRule> findById(Long id);

    List<DeviationRule> findAll();

    DeviationRule save(DeviationRule rule);
}
    