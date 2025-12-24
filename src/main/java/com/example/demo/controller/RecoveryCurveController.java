package com.example.demo.controller;

import com.example.demo.model.RecoveryCurveProfile;
import com.example.demo.service.RecoveryCurveService;
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
import java.util.List;

@RestController
@RequestMapping("/api/curves")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Recovery Curves", description = "Recovery curve management endpoints")
public class RecoveryCurveController {

    private final RecoveryCurveService recoveryCurveService;

    @Operation(summary = "Create a new recovery curve entry", 
               description = "Creates a new expected recovery profile for a surgery type and day")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recovery curve entry created",
                    content = @Content(schema = @Schema(implementation = RecoveryCurveProfile.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Entry already exists for this surgery type and day")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<RecoveryCurveProfile> createRecoveryCurve(
            @Parameter(description = "Recovery curve details", required = true)
            @Valid @RequestBody RecoveryCurveProfile curve) {
        
        log.info("Creating recovery curve entry for surgery type: {}, day: {}", 
                curve.getSurgeryType(), curve.getDayNumber());
        
        RecoveryCurveProfile createdCurve = recoveryCurveService.createCurveEntry(curve);
        
        log.info("Recovery curve entry created with ID: {}", createdCurve.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCurve);
    }

    @Operation(summary = "Get recovery curve by surgery type", 
               description = "Returns all recovery curve entries for a specific surgery type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recovery curve retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecoveryCurveProfile.class)))),
        @ApiResponse(responseCode = "404", description = "No curves found for this surgery type")
    })
    @GetMapping("/surgery-type/{surgeryType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<RecoveryCurveProfile>> getCurveBySurgeryType(
            @Parameter(description = "Surgery type", required = true)
            @PathVariable String surgeryType) {
        
        log.info("Fetching recovery curve for surgery type: {}", surgeryType);
        
        List<RecoveryCurveProfile> curves = recoveryCurveService.getCurveForSurgery(surgeryType);
        
        if (curves.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(curves);
    }

    @Operation(summary = "Get all recovery curves", 
               description = "Returns all recovery curve entries")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All recovery curves retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecoveryCurveProfile.class))))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<RecoveryCurveProfile>> getAllRecoveryCurves() {
        
        log.info("Fetching all recovery curves");
        
        List<Reco