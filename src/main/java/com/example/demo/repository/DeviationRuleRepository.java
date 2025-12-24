package com.example.demo.repository;

import com.example.demo.model.DeviationRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviationRuleRepository extends JpaRepository<DeviationRule, Long> {

    Optional<DeviationRule> findByRuleCode(String ruleCode);

    List<DeviationRule> findByActiveTrue();
}
