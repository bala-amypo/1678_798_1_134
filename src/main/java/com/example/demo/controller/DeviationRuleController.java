package com.example.demo.controller;

import com.example.demo.model.DeviationRule;
import com.example.demo.service.DeviationRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deviation-rules")
@Tag(name = "Deviation Rules", description = "Deviation rule management")
public class DeviationRuleController {
    private final DeviationRuleService deviationRuleService;

    public DeviationRuleController(DeviationRuleService deviationRuleService) {
        this.deviationRuleService = deviationRuleService;
    }

    @PostMapping
    public ResponseEntity<DeviationRule> createRule(@Valid @RequestBody DeviationRule rule) {
        DeviationRule created = deviationRuleService.createRule(rule);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviationRule> updateRule(@PathVariable Long id, @Valid @RequestBody DeviationRule rule) {
        DeviationRule updated = deviationRuleService.updateRule(id, rule);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DeviationRule>> getActiveRules() {
        List<DeviationRule> rules = deviationRuleService.getActiveRules();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviationRule> getRuleById(@PathVariable Long id) {
        Optional<DeviationRule> rule = deviationRuleService.getRuleById(id);
        return rule.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DeviationRule>> getAllRules() {
        List<DeviationRule> rules = deviationRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }
}