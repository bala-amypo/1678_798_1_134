package com.example.demo.service;

import com.example.demo.model.DeviationRule;

import java.util.List;
import java.util.Optional;

public interface DeviationRuleService {

    DeviationRule createRule(DeviationRule rule);

    DeviationRule updateRule(Long id, DeviationRule updated);

    List<DeviationRule> getActiveRules();

    Optional<DeviationRule> getRuleByCode(String code);
}
