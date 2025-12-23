package com.example.demo.repository;

import com.example.demo.model.DeviationRule;

import java.util.List;

public interface DeviationRuleRepository {

    List<DeviationRule> findBySurgeryType(String surgeryType);

    List<DeviationRule> findAll();

    DeviationRule save(DeviationRule rule);
}
