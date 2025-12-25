package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/recovery-curves")
public class RecoveryCurveController {

    @GetMapping
    public ResponseEntity<String> getAllCurves() {
        return ResponseEntity.ok("Recovery curve service is running");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Recovery curve service health check");
    }
}