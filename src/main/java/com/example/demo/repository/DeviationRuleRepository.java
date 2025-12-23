package com.example.demo.repository;

import com.example.demo.model.DeviationRule;

import java.util.List;
import java.util.Optional;

public interface DeviationRuleRepository {

    Optional<DeviationRule> findById(Long id);

    Optional<DeviationRule> findByRuleCode(String ruleCode);

    List<DeviationRule> findByActiveTrue();

    DeviationRule save(DeviationRule rule);
}
