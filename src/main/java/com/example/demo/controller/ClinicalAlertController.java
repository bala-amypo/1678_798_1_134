package com.example.demo.controller;

import com.example.demo.model.ClinicalAlertRecord;
import com.example.demo.service.ClinicalAlertService;
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
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clinical Alerts", description = "Endpoints for managing clinical alerts")
public class ClinicalAlertController {

    private final ClinicalAlertService clinicalAlertService;

    @GetMapping
    @Operation(summary = "Get all clinical alerts")
    public ResponseEntity<List<ClinicalAlertRecord>> getAllAlerts() {
        List<ClinicalAlertRecord> alerts = clinicalAlertService.getAllAlerts();
       