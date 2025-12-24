package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DeviationRule;
import com.example.demo.repository.DeviationRuleRepository;
import com.example.demo.service.DeviationRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviationRuleServiceImpl implements DeviationRuleService {
    
    private final DeviationRuleRepository deviationRuleRepository;
    
    @Override
    @Transactional
    public DeviationRule createRule(DeviationRule rule) {
        // Validate required fields
        if (rule.getRuleCode() == null || rule.getRuleCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Rule code is required");
        }
        
        if (rule.getParameter() == null || rule.getParameter().trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter is required");
        }
        
        if (rule.getThreshold() == null || rule.getThreshold() < 0) {
            throw new IllegalArgumentException("Threshold must be non-negative");
        }
        
        if (rule.getSeverity() == null || rule.getSeverity().trim().isEmpty()) {
            throw new IllegalArgumentException("Severity is required");
        }
        
        // Check if rule code already exists
        if (deviationRuleRepository.findByRuleCode(rule.getRuleCode()).isPresent()) {
            throw new IllegalArgumentException("Rule code already exists: " + rule.getRuleCode());
        }
        
        // Validate parameter is one of allowed values
        if (!isValidParameter(rule.getParameter())) {
            throw new IllegalArgumentException("Parameter must be one of: PAIN, MOBILITY, FATIGUE");
        }
        
        // Validate severity is one of allowed values
        if (!isValidSeverity(rule.getSeverity())) {
            throw new IllegalArgumentException("Severity must be one of: LOW, MEDIUM, HIGH");
        }
        
        return deviationRuleRepository.save(rule);
    }
    
    @Override
    public List<DeviationRule> getAllRules() {
        return deviationRuleRepository.findAll();
    }
    
    @Override
    public Optional<DeviationRule> getRuleByCode(String ruleCode) {
        return deviationRuleRepository.findByRuleCode(ruleCode);
    }
    
    @Override
    public DeviationRule getRuleById(Long id) {
        return deviationRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deviation rule not found with id: " + id));
    }
    
    @Override
    @Transactional
    public DeviationRule updateRule(Long id, DeviationRule rule) {
        DeviationRule existing = getRuleById(id);
        
        // Validate parameter if provided
        if (rule.getParameter() != null && !isValidParameter(rule.getParameter())) {
            throw new IllegalArgumentException("Parameter must be one of: PAIN, MOBILITY, FATIGUE");
        }
        
        // Validate severity if provided
        if (rule.getSeverity() != null && !isValidSeverity(rule.getSeverity())) {
            throw new IllegalArgumentException("Severity must be one of: LOW, MEDIUM, HIGH");
        }
        
        // Update fields if provided
        if (rule.getRuleCode() != null) {
            existing.setRuleCode(rule.getRuleCode());
        }
        if (rule.getParameter() != null) {
            existing.setParameter(rule.getParameter());
        }
        if (rule.getThreshold() != null) {
            existing.setThreshold(rule.getThreshold());
        }
        if (rule.getSeverity() != null) {
            existing.setSeverity(rule.getSeverity());
        }
        if (rule.getActive() != null) {
            existing.setActive(rule.getActive());
        }
        
        return deviationRuleRepository.save(existing);
    }
    
    @Override
    @Transactional
    public void deleteRule(Long id) {
        DeviationRule rule = getRuleById(id);
        deviationRuleRepository.delete(rule);
    }
    
    @Override
    public List<DeviationRule> getActiveRules() {
        return deviationRuleRepository.findByActiveTrue();
    }
    
    @Override
    public List<DeviationRule> getRulesByParameter(String parameter) {
        if (!isValidParameter(parameter)) {
            throw new IllegalArgumentException("Parameter must be one of: PAIN, MOBILITY, FATIGUE");
        }
        return deviationRuleRepository.findByParameterAndActiveTrue(parameter);
    }
    
    private boolean isValidParameter(String parameter) {
        return parameter != null && 
               (parameter.equalsIgnoreCase("PAIN") || 
                parameter.equalsIgnoreCase("MOBILITY") || 
                parameter.equalsIgnoreCase("FATIGUE"));
    }
    
    private boolean isValidSeverity(String severity) {
        return severity != null && 
               (severity.equalsIgnoreCase("LOW") || 
                severity.equalsIgnoreCase("MEDIUM") || 
                severity.equalsIgnoreCase("HIGH"));
    }
}