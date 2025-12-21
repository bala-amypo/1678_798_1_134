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
    private final DeviationRuleRepository deviationRuleRepository;

    public DeviationRuleServiceImpl(DeviationRuleRepository deviationRuleRepository) {
        this.deviationRuleRepository = deviationRuleRepository;
    }

    @Override
    public DeviationRule createRule(DeviationRule rule) {
        if (rule.getThreshold() == null || rule.getThreshold() <= 0) {
            throw new IllegalArgumentException("Threshold must be");
        }
        return deviationRuleRepository.save(rule);
    }

    @Override
    public Optional<DeviationRule> getRuleByCode(String ruleCode) {
        return deviationRuleRepository.findByRuleCode(ruleCode);
    }

    @Override
    public List<DeviationRule> getRulesBySurgery(String surgeryType) {
        return deviationRuleRepository.findBySurgeryType(surgeryType);
    }

    @Override
    public List<DeviationRule> getRulesBySurgeryType(String surgeryType) {
        return deviationRuleRepository.findBySurgeryType(surgeryType);
    }

    @Override
    public List<DeviationRule> getActiveRules() {
        return deviationRuleRepository.findByActiveTrue();
    }

    @Override
    public DeviationRule updateRule(Long id, DeviationRule rule) {
        DeviationRule existing = deviationRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
        
        existing.setSurgeryType(rule.getSurgeryType());
        existing.setParameter(rule.getParameter());
        existing.setThreshold(rule.getThreshold());
        existing.setSeverity(rule.getSeverity());
        existing.setActive(rule.getActive());
        
        return deviationRuleRepository.save(existing);
    }

    @Override
    public List<DeviationRule> getAllRules() {
        return deviationRuleRepository.findAll();
    }

    @Override
    public Optional<DeviationRule> getRuleById(Long id) {
        return deviationRuleRepository.findById(id);
    }
}