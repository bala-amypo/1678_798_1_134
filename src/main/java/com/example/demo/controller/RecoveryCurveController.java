package com.example.demo.controller;

import com.example.demo.model.RecoveryCurveProfile;
import com.example.demo.service.RecoveryCurveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recovery-curves")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Recovery Curves", description = "Endpoints for managing recovery curves")
public class RecoveryCurveController {

    private final RecoveryCurveService recoveryCurveService;

    @PostMapping
    @Operation(summary = "Create a new recovery curve entry")
    public ResponseEntity<RecoveryCurveProfile> createRecoveryCurve(@Valid @RequestBody RecoveryCurveProfile curve) {
        RecoveryCurveProfile created = recoveryCurveService.createCurveEntry(curve);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all recovery curves")
    public ResponseEntity<List<RecoveryCurveProfile>> getAllRecoveryCurves() {
        List<RecoveryCurveProfile> curves = recoveryCurveService.getAllCurves();
        return ResponseEntity.ok(curves);
    }

    @GetMapping("/surgery/{surgeryType}")
    @Operation(summary = "Get recovery curve for specific surgery type")
    public ResponseEntity<List<RecoveryCurveProfile>> getCurveBySurgeryType(@PathVariable String surgeryType) {
        List<RecoveryCurveProfile> curves = recoveryCurveService.getCurveForSurgery(surgeryType);
        return ResponseEntity.ok(curves);
    }

    @GetMapping("/surgery/{surgeryType}/day/{dayNumber}")
    @Operation(summary = "Get recovery curve entry for specific surgery type and day")
    public ResponseEntity<RecoveryCurveProfile> getCurveBySurgeryAndDay(
            @PathVariable String surgeryType,
            @PathVariable Integer dayNumber) {
        return recoveryCurveService.getCurveEntry(surgeryType, dayNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update recovery curve entry")
    public ResponseEntity<RecoveryCurveProfile> updateRecoveryCurve(
            @PathVariable Long id,
            @Valid @RequestBody RecoveryCurveProfile curve) {
        RecoveryCurveProfile updated = recoveryCurveService.updateCurveEntry(id, curve);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete recovery curve entry")
    public ResponseEntity<Void> deleteRecoveryCurve(@PathVariable Long id) {
        recoveryCurveService.deleteCurveEntry(id);
        return ResponseEntity.noContent().build();
    }
}