package com.example.demo.service;

import com.example.demo.model.DeviationRule;

import java.util.List;
import java.util.Optional;

public interface DeviationRuleService {
    DeviationRule createRule(DeviationRule rule);
    Optional<DeviationRule> getRuleByCode(String code);
    List<DeviationRule> getActiveRules();
    DeviationRule updateRule(Long id, DeviationRule rule);
}