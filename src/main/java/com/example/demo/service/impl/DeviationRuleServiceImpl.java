package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DeviationRule;
import com.example.demo.repository.DeviationRuleRepository;
import com.example.demo.service.DeviationRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
    public DeviationRule updateRule(Long id, DeviationRule rule) {
        DeviationRule existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
        rule.setId(existing.getId());
        return repository.save(rule);
    }

    @Override
    public List<DeviationRule> getAllRules() {
        return repository.findAll();
    }

    @Override
    public List<DeviationRule> getActiveRules() {
        return repository.findByActiveTrue();
    }

    @Override
    public Optional<DeviationRule> getRuleByCode(String code) {
        return repository.findByRuleCode(code);
    }
}
