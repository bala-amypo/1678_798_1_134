package com.example.demo.service.impl;

import com.example.demo.model.DeviationRule;
import com.example.demo.repository.DeviationRuleRepository;
import com.example.demo.service.DeviationRuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service   // 🔴 REQUIRED
public class DeviationRuleServiceImpl implements DeviationRuleService {

    private final DeviationRuleRepository repository;

    public DeviationRuleServiceImpl(DeviationRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public DeviationRule createRule(DeviationRule rule) {
        return repository.save(rule);
    }

    @Override
    public List<DeviationRule> getRulesForSurgery(String surgeryType) {
        return repository.findBySurgeryType(surgeryType);
    }

    @Override
    public List<DeviationRule> getAllRules() {
        return repository.findAll();
    }
}
