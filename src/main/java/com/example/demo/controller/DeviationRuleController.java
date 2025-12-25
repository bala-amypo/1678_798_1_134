package com.example.demo.controller;

import com.example.demo.model.DeviationRule;
import com.example.demo.service.DeviationRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Deviation Rules", description = "Endpoints for managing deviation rules")
public class DeviationRuleController {

    private final DeviationRuleService deviationRuleService;

    @PostMapping
    @Operation(summary = "Create a new deviation rule")
    public ResponseEntity<DeviationRule> createDeviationRule(@Valid @RequestBody DeviationRule rule) {
        DeviationRule created = deviationRuleService.createRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all deviation rules")
    public ResponseEntity<List<DeviationRule>> getAllDeviationRules() {
        List<DeviationRule> rules = deviationRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active deviation rules")
    public ResponseEntity<List<DeviationRule>> getActiveDeviationRules() {
        List<DeviationRule> rules = deviationRuleService.getActiveRules();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get deviation rule by ID")
    public ResponseEntity<DeviationRule> getDeviationRuleById(@PathVariable Long id) {
        return deviationRuleService.getRuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{ruleCode}")
    @Operation(summary = "Get deviation rule by code")
    public ResponseEntity<DeviationRule> getDeviationRuleByCode(@PathVariable String ruleCode) {
        Optional<DeviationRule> rule = deviationRuleService.getRuleByCode(ruleCode);
        return rule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update deviation rule")
    public ResponseEntity<DeviationRule> updateDeviationRule(
            @PathVariable Long id,
            @Valid @RequestBody DeviationRule rule) {
        DeviationRule updated = deviationRuleService.updateRule(id, rule);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update deviation rule status")
    public ResponseEntity<DeviationRule> updateDeviationRuleStatus(
            @PathVariable Long id,
            @RequestParam Boolean active) {
        DeviationRule updated = deviationRuleService.updateRuleStatus(id, active);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete deviation rule")
    public ResponseEntity<Void> deleteDeviationRule(@PathVariable Long id) {
        deviationRuleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}