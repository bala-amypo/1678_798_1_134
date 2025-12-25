package com.example.demo.service;

import com.example.demo.model.DeviationRule;
import java.util.List;
import java.util.Optional;

public interface DeviationRuleService {
    DeviationRule createRule(DeviationRule rule);
    List<DeviationRule> getAllRules();
    List<DeviationRule> getActiveRules();
    List<DeviationRule> getRulesByParameter(String parameter);
    List<DeviationRule> getHighPriorityRules();
    Optional<DeviationRule> getRuleById(Long id);
    Optional<DeviationRule> getRuleByCode(String ruleCode);
    DeviationRule updateRule(Long id, DeviationRule rule);
    DeviationRule updateRuleStatus(Long id, Boolean active);
    void deleteRule(Long id);
    List<DeviationRule> evaluateRules(Integer actualValue, Integer expectedValue, String parameter);
    boolean checkRuleViolation(DeviationRule rule, Integer actualValue, Integer expectedValue);
    Long countActiveRules();
    List<DeviationRule> getRulesRequiringAction();
    List<DeviationRule> searchRules(String keyword);
}