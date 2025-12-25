package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/symptom-logs")
public class SymptomLogController {

    @GetMapping
    public ResponseEntity<String> getAllLogs() {
        return ResponseEntity.ok("Symptom log service is running");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Symptom log service health check");
    }
}