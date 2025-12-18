package com.example.demo.repository;

import com.example.demo.model.DeviationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviationRuleRepository
        extends JpaRepository<DeviationRule, Long> {
    List<DeviationRule> findBySurgeryType(String surgeryType);
}
