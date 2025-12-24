package com.example.demo.controller;

import com.example.demo.model.DeviationRule;
import com.example.demo.service.DeviationRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class DeviationRuleController {

    private final DeviationRuleService deviationRuleService;

    @PostMapping
    public ResponseEntity<DeviationRule> create(
            @RequestBody DeviationRule rule) {
        return ResponseEntity.ok(deviationRuleService.createRule(rule));
    }

    @GetMapping("/active")
    public ResponseEntity<List<DeviationRule>> getActiveRules() {
        return ResponseEntity.ok(deviationRuleService.getActiveRules());
    }

    @GetMapping("/{code}")
    public ResponseEntity<DeviationRule> getByCode(
            @PathVariable String code) {
        return deviationRuleService.getRuleByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviationRule> update(
            @PathVariable Long id,
            @RequestBody DeviationRule rule) {
        return ResponseEntity.ok(
                deviationRuleService.updateRule(id, rule));
    }
}
