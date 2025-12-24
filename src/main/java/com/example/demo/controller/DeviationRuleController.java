package com.example.demo.controller;

import com.example.demo.model.DeviationRule;
import com.example.demo.service.DeviationRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Deviation Rules", description = "Deviation rule management endpoints")
public class DeviationRuleController {

    private final DeviationRuleService deviationRuleService;

    @Operation(summary = "Create a new deviation rule", 
               description = "Creates a new rule for detecting deviations in patient symptoms")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Deviation rule created",
                    content = @Content(schema = @Schema(implementation = DeviationRule.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Rule code already exists")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<DeviationRule> createDeviationRule(
            @Parameter(description = "Deviation rule details", required = true)
            @Valid @RequestBody DeviationRule rule) {
        
        log.info("Creating deviation rule with code: {}", rule.getRuleCode());
        
        DeviationRule createdRule = deviationRuleService.createRule(rule);
        
        log.info("Deviation rule created with ID: {}", createdRule.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
    }

    @Operation(summary = "Get all deviation rules", 
               description = "Returns all deviation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All deviation rules retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviationRule.class))))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<DeviationRule>> getAllDeviationRules() {
        
        log.info("Fetching all deviation rules");
        
        List<DeviationRule> rules = deviationRuleService.getAllRules();
        
        return ResponseEntity.ok(rules);
    }

    @Operation(summary = "Get deviation rule by ID", 
               description = "Returns a single deviation rule by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deviation rule found",
                    content = @Content(schema = @Schema(implementation = DeviationRule.class))),
        @ApiResponse(responseCode = "404", description = "Deviation rule not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<DeviationRule> getDeviationRuleById(
            @Parameter(description = "Deviation rule ID", required = true)
            @PathVariable Long id) {
        
        log.info("Fetching deviation rule with ID: {}", id);
        
        DeviationRule rule = deviationRuleService.getRuleById(id);
        
        return ResponseEntity.ok(rule);
    }

    @Operation(summary = "Get deviation rule by code", 
               description = "Returns a deviation rule by its unique code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deviation rule found",
                    content = @Content(schema = @Schema(implementation = DeviationRule.class))),
        @ApiResponse(responseCode = "404", description = "Deviation rule not found")
    })
    @GetMapping("/code/{ruleCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<DeviationRule> getDeviationRuleByCode(
            @Parameter(description = "Deviation rule code", required = true)
            @PathVariable String ruleCode) {
        
        log.info("Fetching deviation rule with code: {}", ruleCode);
        
        Optional<DeviationRule> rule = deviationRuleService.getRuleByCode(ruleCode);
        
        return rule.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update deviation rule", 
               description = "Updates an existing deviation rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deviation rule updated",
                    content = @Content(schema = @Schema(implementation = DeviationRule.class))),
        @ApiResponse(responseCode = "404", description = "Deviation rule not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<DeviationRule> updateDeviationRule(
            @Parameter(description = "Deviation rule ID", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Updated deviation rule details", required = true)
            @Valid @RequestBody DeviationRule ruleDetails) {
        
        log.info("Updating deviation rule with ID: {}", id);
        
        DeviationRule updatedRule = deviationRuleService.updateRule(id, ruleDetails);
        
        log.info("Deviation rule updated successfully for ID: {}", id);
        
        return ResponseEntity.ok(updatedRule);
    }

    @Operation(summary = "Delete deviation rule", 
               description = "Deletes a deviation rule by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Deviation rule deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Deviation rule not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDeviationRule(
            @Parameter(description = "Deviation rule ID", required = true)
            @PathVariable Long id) {
        
        log.info("Deleting deviation rule with ID: {}", id);
        
        deviationRuleService.deleteRule(id);
        
        log.info("Deviation rule deleted successfully for ID: {}", id);
        
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get active deviation rules", 
               description = "Returns all active deviation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active deviation rules retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviationRule.class))))
    })
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<DeviationRule>> getActiveDeviationRules() {
        
        log.info("Fetching all active deviation rules");
        
        List<DeviationRule> activeRules = deviationRuleService.getActiveRules();
        
        return ResponseEntity.ok(activeRules);
    }

    @Operation(summary = "Get deviation rules by parameter", 
               description = "Returns deviation rules filtered by parameter type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deviation rules retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviationRule.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid parameter type")
    })
    @GetMapping("/parameter/{parameter}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<DeviationRule>> getDeviationRulesByParameter(
            @Parameter(description = "Parameter type (PAIN, MOBILITY, FATIGUE)", required = true)
            @PathVariable String parameter) {
        
        log.info("Fetching deviation rules for parameter: {}", parameter);
        
        List<DeviationRule> rules = deviationRuleService.getRulesByParameter(parameter);
        
        return ResponseEntity.ok(rules);
    }

    @Operation(summary = "Toggle rule activation", 
               description = "Activates or deactivates a deviation rule")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rule activation toggled",
                    content = @Content(schema = @Schema(implementation = DeviationRule.class))),
        @ApiResponse(responseCode = "404", description = "Deviation rule not found")
    })
    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<DeviationRule> toggleRuleActivation(
            @Parameter(description = "Deviation rule ID", required = true)
            @PathVariable Long id) {
        
        log.info("Toggling activation for deviation rule with ID: {}", id);
        
        DeviationRule rule = deviationRuleService.getRuleById(id);
        rule.setActive(!rule.getActive());
        
        // Note: In a real app, you would have a service method for this
        DeviationRule updatedRule = deviationRuleService.updateRule(id, rule);
        
        log.info("Deviation rule activation toggled for ID: {} - Active: {}", 
                id, updatedRule.getActive());
        
        return ResponseEntity.ok(updatedRule);
    }

    @Operation(summary = "Bulk update rule thresholds", 
               description = "Updates thresholds for multiple rules at once")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thresholds updated successfully")
    })
    @PutMapping("/bulk-thresholds")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<Map<String, Object>> bulkUpdateThresholds(
            @Parameter(description = "Map of rule IDs to new thresholds", required = true)
            @RequestBody Map<Long, Integer> thresholdUpdates) {
        
        log.info("Bulk updating thresholds for {} rules", thresholdUpdates.size());
        
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failureCount = 0;
        
        for (Map.Entry<Long, Integer> entry : thresholdUpdates.entrySet()) {
            try {
                DeviationRule rule = deviationRuleService.getRuleById(entry.getKey());
                rule.setThreshold(entry.getValue());
                deviationRuleService.updateRule(entry.getKey(), rule);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to update rule {}: {}", entry.getKey(), e.getMessage());
                failureCount++;
            }
        }
        
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("message", String.format("Updated %d rules, failed %d", successCount, failureCount));
        
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Validate symptom against rules", 
               description = "Checks if a symptom value would trigger any deviation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation results returned")
    })
    @PostMapping("/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<Map<String, Object>> validateSymptom(
            @Parameter(description = "Parameter to validate", required = true)
            @RequestParam String parameter,
            
            @Parameter(description = "Symptom value", required = true)
            @RequestParam Integer value) {
        
        log.info("Validating symptom: {} = {}", parameter, value);
        
        Map<String, Object> validationResult = new HashMap<>();
        validationResult.put("parameter", parameter);
        validationResult.put("value", value);
        
        List<DeviationRule> applicableRules = deviationRuleService.getRulesByParameter(parameter);
        
        List<Map<String, Object>> triggeredRules = applicableRules.stream()
                .filter(rule -> value > rule.getThreshold())
                .map(rule -> {
                    Map<String, Object> ruleInfo = new HashMap<>();
                    ruleInfo.put("ruleCode", rule.getRuleCode());
                    ruleInfo.put("threshold", rule.getThreshold());
                    ruleInfo.put("severity", rule.getSeverity());
                    ruleInfo.put("exceededBy", value - rule.getThreshold());
                    return ruleInfo;
                })
                .toList();
        
        validationResult.put("wouldTriggerRules", !triggeredRules.isEmpty());
        validationResult.put("triggeredRules", triggeredRules);
        validationResult.put("applicableRulesCount", applicableRules.size());
        
        return ResponseEntity.ok(validationResult);
    }

    @Operation(summary = "Get rule statistics", 
               description = "Returns statistics about deviation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved")
    })
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<Map<String, Object>> getRuleStatistics() {
        
        log.info("Fetching deviation rule statistics");
        
        List<DeviationRule> allRules = deviationRuleService.getAllRules();
        
        Map<String, Object> statistics = new HashMap<>();
        
        long activeCount = allRules.stream().filter(DeviationRule::getActive).count();
        long inactiveCount = allRules.size() - activeCount;
        
        Map<String, Long> rulesByParameter = allRules.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        DeviationRule::getParameter,
                        java.util.stream.Collectors.counting()));
        
        Map<String, Long> rulesBySeverity = allRules.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        DeviationRule::getSeverity,
                        java.util.stream.Collectors.counting()));
        
        statistics.put("totalRules", allRules.size());
        statistics.put("activeRules", activeCount);
        statistics.put("inactiveRules", inactiveCount);
        statistics.put("rulesByParameter", rulesByParameter);
        statistics.put("rulesBySeverity", rulesBySeverity);
        statistics.put("averageThreshold", allRules.stream()
                .mapToInt(DeviationRule::getThreshold)
                .average()
                .orElse(0.0));
        
        return ResponseEntity.ok(statistics);
    }
}