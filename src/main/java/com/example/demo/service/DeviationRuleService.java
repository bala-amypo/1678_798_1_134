package com.example.demo.service;

import com.example.demo.model.DeviationRule;
import java.util.List;
import java.util.Optional;

public interface DeviationRuleService {
    DeviationRule createRule(DeviationRule rule);
    List<DeviationRule> getAllRules();
    Optional<DeviationRule> getRuleByCode(String ruleCode);
    DeviationRule getRuleById(Long id);
    DeviationRule updateRule(Long id, DeviationRule rule);
    void deleteRule(Long id);
    List<DeviationRule> getActiveRules();
    List<DeviationRule> getRulesByParameter(String parameter);
}