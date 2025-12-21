package com.example.demo.controller;

import com.example.demo.model.DeviationRule;
import com.example.demo.service.DeviationRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deviation-rules")
@Tag(name = "Deviation Rules")
public class DeviationRuleController {

    private final DeviationRuleService service;

    public DeviationRuleController(DeviationRuleService service) {
        this.service = service;
    }

    @PostMapping
    public DeviationRule create(@RequestBody DeviationRule rule) {
        return service.createRule(rule);
    }

    @PutMapping("/{id}")
    public DeviationRule update(
            @PathVariable Long id,
            @RequestBody DeviationRule rule) {
        return service.updateRule(id, rule);
    }

    @GetMapping("/active")
    public List<DeviationRule> getActive() {
        return service.getActiveRules();
    }

    @GetMapping
    public List<DeviationRule> getAll() {
        return service.getAllRules();
    }
}
