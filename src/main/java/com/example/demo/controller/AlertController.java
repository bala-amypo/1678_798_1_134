package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @GetMapping
    public ResponseEntity<String> getAllAlerts() {
        return ResponseEntity.ok("Alert service is running");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Alert service health check");
    }
}