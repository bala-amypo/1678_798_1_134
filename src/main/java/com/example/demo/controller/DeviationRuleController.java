package com.example.demo.controller;

import com.example.demo.model.DeviationRule;
import com.example.demo.service.DeviationRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deviation-rules")
public class DeviationRuleController {

    private final DeviationRuleService service;

    public DeviationRuleController(DeviationRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DeviationRule> create(
            @RequestBody DeviationRule rule) {
        return ResponseEntity.ok(service.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviationRule> update(
            @PathVariable Long id,
            @RequestBody DeviationRule rule) {
        return ResponseEntity.ok(service.updateRule(id, rule));
    }

    @GetMapping("/active")
    public ResponseEntity<List<DeviationRule>> getActive() {
        return ResponseEntity.ok(service.getActiveRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviationRule> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                service.getRulesBySurgery(null)
                        .stream()
                        .filter(r -> r.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException("not found"))
        );
    }

    @GetMapping
    public ResponseEntity<List<DeviationRule>> getAll() {
        return ResponseEntity.ok(service.getActiveRules());
    }
}
