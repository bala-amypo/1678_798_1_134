package com.example.demo.service.impl;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DeviationRule;
import com.example.demo.repository.DeviationRuleRepository;
import com.example.demo.service.DeviationRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeviationRuleServiceImpl implements DeviationRuleService {
    
    private final DeviationRuleRepository deviationRuleRepository;
    
    @Override
    public DeviationRule createRule(DeviationRule rule) {
        log.info("Creating deviation rule with code: {}", rule.getRuleCode());
        
        // Check if rule code already exists
        if (deviationRuleRepository.existsByRuleCode(rule.getRuleCode())) {
            throw new DuplicateResourceException("Deviation rule", "ruleCode", rule.getRuleCode());
        }
        
        // Set default values
        if (rule.getActive() == null) {
            rule.setActive(true);
        }
        if (rule.getOperator() == null) {
            rule.setOperator("GREATER_THAN");
        }
        if (rule.getSeverity() == null) {
            rule.setSeverity("MEDIUM");
        }
        if (rule.getActionRequired() == null) {
            rule.setActionRequired(false);
        }
        if (rule.getNotificationChannels() == null) {
            rule.setNotificationChannels("IN_APP");
        }
        if (rule.getCreatedAt() == null) {
            rule.setCreatedAt(LocalDateTime.now());
        }
        
        // Validate thresholds for BETWEEN operator
        if ("BETWEEN".equals(rule.getOperator()) && rule.getMaxThreshold() == null) {
            throw new IllegalArgumentException("Max threshold is required for BETWEEN operator");
        }
        
        DeviationRule savedRule = deviationRuleRepository.save(rule);
        log.info("Deviation rule created with ID: {}", savedRule.getId());
        
        return savedRule;
    }
    
    @Override
    public List<DeviationRule> getAllRules() {
        log.debug("Fetching all deviation rules");
        return deviationRuleRepository.findAll();
    }
    
    @Override
    public List<DeviationRule> getActiveRules() {
        log.debug("Fetching active deviation rules");
        return deviationRuleRepository.findByActiveTrue();
    }
    
    @Override
    public List<DeviationRule> getRulesByParameter(String parameter) {
        log.debug("Fetching deviation rules for parameter: {}", parameter);
        return deviationRuleRepository.findByParameterAndActiveTrue(parameter);
    }
    
    @Override
    public List<DeviationRule> getHighPriorityRules() {
        log.debug("Fetching high priority deviation rules");
        return deviationRuleRepository.findHighPriorityRules();
    }
    
    @Override
    public Optional<DeviationRule> getRuleById(Long id) {
        log.debug("Fetching deviation rule by ID: {}", id);
        return deviationRuleRepository.findById(id);
    }
    
    @Override
    public Optional<DeviationRule> getRuleByCode(String ruleCode) {
        log.debug("Fetching deviation rule by code: {}", ruleCode);
        return deviationRuleRepository.findByRuleCode(ruleCode);
    }
    
    @Override
    public DeviationRule updateRule(Long id, DeviationRule rule) {
        log.info("Updating deviation rule ID: {}", id);
        
        DeviationRule existing = deviationRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deviation rule", "id", id));
        
        // Check if rule code is being changed and if it already exists
        if (rule.getRuleCode() != null && !rule.getRuleCode().equals(existing.getRuleCode())) {
            if (deviationRuleRepository.existsByRuleCode(rule.getRuleCode())) {
                throw new DuplicateResourceException("Deviation rule", "ruleCode", rule.getRuleCode());
            }
            existing.setRuleCode(rule.getRuleCode());
        }
        
        // Update other fields if provided
        if (rule.getParameter() != null) {
            existing.setParameter(rule.getParameter());
        }
        if (rule.getThreshold() != null) {
            existing.setThreshold(rule.getThreshold());
        }
        if (rule.getMaxThreshold() != null) {
            existing.setMaxThreshold(rule.getMaxThreshold());
        }
        if (rule.getOperator() != null) {
            existing.setOperator(rule.getOperator());
        }
        if (rule.getSeverity() != null) {
            existing.setSeverity(rule.getSeverity());
        }
        if (rule.getAlertMessageTemplate() != null) {
            existing.setAlertMessageTemplate(rule.getAlertMessageTemplate());
        }
        if (rule.getDescription() != null) {
            existing.setDescription(rule.getDescription());
        }
        if (rule.getActive() != null) {
            existing.setActive(rule.getActive());
        }
        if (rule.getNotificationChannels() != null) {
            existing.setNotificationChannels(rule.getNotificationChannels());
        }
        if (rule.getActionRequired() != null) {
            existing.setActionRequired(rule.getActionRequired());
        }
        if (rule.getActionInstructions() != null) {
            existing.setActionInstructions(rule.getActionInstructions());
        }
        
        // Set updated timestamp
        existing.setUpdatedAt(LocalDateTime.now());
        
        DeviationRule updatedRule = deviationRuleRepository.save(existing);
        log.info("Deviation rule updated successfully");
        
        return updatedRule;
    }
    
    @Override
    public DeviationRule updateRuleStatus(Long id, Boolean active) {
        log.info("Updating deviation rule status. ID: {}, Active: {}", id, active);
        
        DeviationRule rule = deviationRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deviation rule", "id", id));
        
        rule.setActive(active);
        rule.setUpdatedAt(LocalDateTime.now());
        
        DeviationRule updatedRule = deviationRuleRepository.save(rule);
        log.info("Deviation rule status updated to: {}", active ? "active" : "inactive");
        
        return updatedRule;
    }
    
    @Override
    public void deleteRule(Long id) {
        log.info("Deleting deviation rule ID: {}", id);
        
        DeviationRule rule = deviationRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deviation rule", "id", id));
        
        deviationRuleRepository.delete(rule);
        log.info("Deviation rule deleted successfully");
    }
    
    @Override
    public List<DeviationRule> evaluateRules(Integer actualValue, Integer expectedValue, String parameter) {
        log.debug("Evaluating rules for parameter: {}, actual: {}, expected: {}", 
                parameter, actualValue, expectedValue);
        
        List<DeviationRule> violatedRules = new ArrayList<>();
        List<DeviationRule> activeRules = getRulesByParameter(parameter);
        
        for (DeviationRule rule : activeRules) {
            if (checkRuleViolation(rule, actualValue, expectedValue)) {
                violatedRules.add(rule);
            }
        }
        
        log.debug("Found {} violated rules for parameter: {}", violatedRules.size(), parameter);
        return violatedRules;
    }
    
    @Override
    public boolean checkRuleViolation(DeviationRule rule, Integer actualValue, Integer expectedValue) {
        if (actualValue == null || expectedValue == null) {
            return false;
        }
        
        int deviation = Math.abs(actualValue - expectedValue);
        return rule.evaluate(deviation);
    }
    
    @Override
    public Long countActiveRules() {
        log.debug("Counting active deviation rules");
        return deviationRuleRepository.findByActiveTrue().stream().count();
    }
    
    @Override
    public List<DeviationRule> getRulesRequiringAction() {
        log.debug("Fetching rules requiring action");
        return deviationRuleRepository.findByActionRequiredTrueAndActiveTrue();
    }
    
    @Override
    public List<DeviationRule> searchRules(String keyword) {
        log.debug("Searching deviation rules with keyword: {}", keyword);
        return deviationRuleRepository.searchByDescription(keyword);
    }
    
    // Helper method to validate rule parameters
    public void validateRuleParameters(DeviationRule rule) {
        if (rule.getRuleCode() == null || rule.getRuleCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Rule code is required");
        }
        
        if (rule.getParameter() == null || rule.getParameter().trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter is required");
        }
        
        if (rule.getThreshold() == null) {
            throw new IllegalArgumentException("Threshold is required");
        }
        
        if (rule.getThreshold() < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }
        
        if ("BETWEEN".equals(rule.getOperator())) {
            if (rule.getMaxThreshold() == null) {
                throw new IllegalArgumentException("Max threshold is required for BETWEEN operator");
            }
            if (rule.getMaxThreshold() <= rule.getThreshold()) {
                throw new IllegalArgumentException("Max threshold must be greater than threshold for BETWEEN operator");
            }
        }
        
        // Validate parameter value
        List<String> validParameters = List.of("PAIN", "MOBILITY", "FATIGUE", "WELLNESS_SCORE");
        if (!validParameters.contains(rule.getParameter())) {
            throw new IllegalArgumentException("Invalid parameter. Must be one of: " + validParameters);
        }
        
        // Validate severity
        List<String> validSeverities = List.of("LOW", "MEDIUM", "HIGH", "CRITICAL");
        if (!validSeverities.contains(rule.getSeverity())) {
            throw new IllegalArgumentException("Invalid severity. Must be one of: " + validSeverities);
        }
        
        // Validate operator
        List<String> validOperators = List.of("GREATER_THAN", "LESS_THAN", "EQUALS", "BETWEEN");
        if (!validOperators.contains(rule.getOperator())) {
            throw new IllegalArgumentException("Invalid operator. Must be one of: " + validOperators);
        }
    }
    
    // Helper method to get rules statistics
    public Map<String, Object> getRulesStatistics() {
        log.debug("Generating deviation rules statistics");
        
        Map<String, Object> statistics = new HashMap<>();
        
        // Total rules
        long totalRules = deviationRuleRepository.count();
        statistics.put("totalRules", totalRules);
        
        // Active rules
        List<DeviationRule> activeRules = getActiveRules();
        statistics.put("activeRules", (long) activeRules.size());
        
        // Rules by parameter
        List<Object[]> rulesByParameter = deviationRuleRepository.countRulesBySeverity();
        Map<String, Long> severityCounts = new HashMap<>();
        
        for (Object[] count : rulesByParameter) {
            String severity = (String) count[0];
            Long countValue = (Long) count[1];
            severityCounts.put(severity, countValue);
        }
        
        statistics.put("rulesBySeverity", severityCounts);
        
        // Rules requiring action
        List<DeviationRule> actionRequiredRules = getRulesRequiringAction();
        statistics.put("rulesRequiringAction", (long) actionRequiredRules.size());
        
        // High priority rules
        List<DeviationRule> highPriorityRules = getHighPriorityRules();
        statistics.put("highPriorityRules", (long) highPriorityRules.size());
        
        return statistics;
    }
    
    // Helper method to activate/deactivate multiple rules
    public int batchUpdateRuleStatus(List<Long> ruleIds, boolean active) {
        log.info("Batch updating {} rules to active: {}", ruleIds.size(), active);
        
        int updatedCount = 0;
        
        for (Long ruleId : ruleIds) {
            try {
                DeviationRule rule = deviationRuleRepository.findById(ruleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Deviation rule", "id", ruleId));
                
                if (rule.getActive() != active) {
                    rule.setActive(active);
                    rule.setUpdatedAt(LocalDateTime.now());
                    deviationRuleRepository.save(rule);
                    updatedCount++;
                }
            } catch (Exception e) {
                log.error("Failed to update rule ID {}: {}", ruleId, e.getMessage());
            }
        }
        
        log.info("Batch updated {} rules", updatedCount);
        return updatedCount;
    }
    
    // Helper method to duplicate a rule
    public DeviationRule duplicateRule(Long ruleId, String newRuleCode) {
        log.info("Duplicating rule ID: {} with new code: {}", ruleId, newRuleCode);
        
        DeviationRule originalRule = deviationRuleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Deviation rule", "id", ruleId));
        
        // Check if new rule code already exists
        if (deviationRuleRepository.existsByRuleCode(newRuleCode)) {
            throw new DuplicateResourceException("Deviation rule", "ruleCode", newRuleCode);
        }
        
        // Create new rule with same parameters but new code
        DeviationRule duplicatedRule = DeviationRule.builder()
                .ruleCode(newRuleCode)
                .parameter(originalRule.getParameter())
                .threshold(originalRule.getThreshold())
                .maxThreshold(originalRule.getMaxThreshold())
                .operator(originalRule.getOperator())
                .severity(originalRule.getSeverity())
                .alertMessageTemplate(originalRule.getAlertMessageTemplate())
                .description(originalRule.getDescription() + " (Duplicated from " + originalRule.getRuleCode() + ")")
                .active(originalRule.getActive())
                .notificationChannels(originalRule.getNotificationChannels())
                .actionRequired(originalRule.getActionRequired())
                .actionInstructions(originalRule.getActionInstructions())
                .createdAt(LocalDateTime.now())
                .createdBy(originalRule.getCreatedBy())
                .build();
        
        DeviationRule savedRule = deviationRuleRepository.save(duplicatedRule);
        log.info("Rule duplicated successfully. New rule ID: {}", savedRule.getId());
        
        return savedRule;
    }
    
    // Helper method to import rules from list
    public List<DeviationRule> importRules(List<DeviationRule> rules, Long createdBy) {
        log.info("Importing {} deviation rules", rules.size());
        
        List<DeviationRule> importedRules = new ArrayList<>();
        
        for (DeviationRule rule : rules) {
            try {
                // Set created by user
                rule.setCreatedBy(createdBy);
                
                // Create rule with validation
                DeviationRule createdRule = createRule(rule);
                importedRules.add(createdRule);
            } catch (Exception e) {
                log.error("Failed to import rule {}: {}", rule.getRuleCode(), e.getMessage());
            }
        }
        
        log.info("Successfully imported {} out of {} rules", importedRules.size(), rules.size());
        return importedRules;
    }
    
    // Helper method to export active rules
    public List<DeviationRule> exportActiveRules() {
        log.debug("Exporting active deviation rules");
        return getActiveRules();
    }
    
    // Helper method to validate rule against actual values
    public Map<String, Object> validateAgainstRule(Long ruleId, Integer actualValue, Integer expectedValue) {
        log.debug("Validating rule ID {} against values: actual={}, expected={}", 
                ruleId, actualValue, expectedValue);
        
        DeviationRule rule = deviationRuleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Deviation rule", "id", ruleId));
        
        Map<String, Object> result = new HashMap<>();
        result.put("rule", rule);
        result.put("actualValue", actualValue);
        result.put("expectedValue", expectedValue);
        
        if (actualValue == null || expectedValue == null) {
            result.put("violated", false);
            result.put("message", "Missing actual or expected value");
            return result;
        }
        
        int deviation = Math.abs(actualValue - expectedValue);
        boolean violated = rule.evaluate(deviation);
        
        result.put("violated", violated);
        result.put("deviation", deviation);
        result.put("withinThreshold", !violated);
        
        if (violated) {
            String message = rule.generateAlertMessage(actualValue, expectedValue);
            result.put("message", message);
            result.put("alertSeverity", rule.getSeverity());
        } else {
            result.put("message", "Values are within acceptable range");
        }
        
        return result;
    }
}