package com.example.demo.repository;

import com.example.demo.model.DeviationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviationRuleRepository extends JpaRepository<DeviationRule, Long> {
    List<DeviationRule> findBySurgeryType(String surgeryType);
    Optional<DeviationRule> findByRuleCode(String ruleCode);
    List<DeviationRule> findByActiveTrue();
    List<DeviationRule> findByActiveFalse();
}