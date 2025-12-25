package com.example.demo.repository;

import com.example.demo.model.DeviationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviationRuleRepository extends JpaRepository<DeviationRule, Long> {
    
    // Find by rule code
    Optional<DeviationRule> findByRuleCode(String ruleCode);
    
    // Find active rules
    List<DeviationRule> findByActiveTrue();
    
    // Find rules by parameter
    List<DeviationRule> findByParameter(String parameter);
    
    // Find active rules by parameter
    List<DeviationRule> findByParameterAndActiveTrue(String parameter);
    
    // Find rules by severity
    List<DeviationRule> findBySeverity(String severity);
    
    // Find active rules by severity
    List<DeviationRule> findBySeverityAndActiveTrue(String severity);
    
    // Find rules by operator
    List<DeviationRule> findByOperator(String operator);
    
    // Find high and critical severity rules
    @Query("SELECT r FROM DeviationRule r WHERE r.active = true AND r.severity IN ('HIGH', 'CRITICAL')")
    List<DeviationRule> findHighPriorityRules();
    
    // Find rules requiring action
    List<DeviationRule> findByActionRequiredTrueAndActiveTrue();
    
    // Find rules by notification channels
    @Query("SELECT r FROM DeviationRule r WHERE r.active = true AND r.notificationChannels LIKE %:channel%")
    List<DeviationRule> findByNotificationChannel(@Param("channel") String channel);
    
    // Find rules created by specific user
    List<DeviationRule> findByCreatedBy(Long userId);
    
    // Find rules updated after specific date
    List<DeviationRule> findByUpdatedAtAfter(java.time.LocalDateTime date);
    
    // Search rules by description
    @Query("SELECT r FROM DeviationRule r WHERE LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) AND r.active = true")
    List<DeviationRule> searchByDescription(@Param("keyword") String keyword);
    
    // Count rules by severity
    @Query("SELECT r.severity, COUNT(r) FROM DeviationRule r WHERE r.active = true GROUP BY r.severity")
    List<Object[]> countRulesBySeverity();
    
    // Find rules with specific threshold range
    @Query("SELECT r FROM DeviationRule r WHERE r.threshold >= :minThreshold AND r.threshold <= :maxThreshold AND r.active = true")
    List<DeviationRule> findByThresholdRange(
            @Param("minThreshold") Integer minThreshold, 
            @Param("maxThreshold") Integer maxThreshold);
    
    // Find rules for specific parameter and severity
    List<DeviationRule> findByParameterAndSeverityAndActiveTrue(String parameter, String severity);
    
    // Check if rule code exists
    Boolean existsByRuleCode(String ruleCode);
    
    // Find rules with BETWEEN operator
    List<DeviationRule> findByOperatorAndActiveTrue(String operator);
}