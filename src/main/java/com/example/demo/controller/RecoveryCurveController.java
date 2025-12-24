package com.example.demo.controller;

import com.example.demo.model.RecoveryCurveProfile;
import com.example.demo.service.RecoveryCurveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recovery-curves")
public class RecoveryCurveController {

    private final RecoveryCurveService service;

    public RecoveryCurveController(RecoveryCurveService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RecoveryCurveProfile> create(
            @RequestBody RecoveryCurveProfile curve) {
        return ResponseEntity.ok(service.createCurveEntry(curve));
    }

    @GetMapping("/surgery/{surgeryType}")
    public ResponseEntity<List<RecoveryCurveProfile>> getBySurgery(
            @PathVariable String surgeryType) {
        return ResponseEntity.ok(service.getCurveForSurgery(surgeryType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecoveryCurveProfile> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                service.getAllCurves()
                        .stream()
                        .filter(c -> c.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException("not found"))
        );
    }

    @GetMapping
    public ResponseEntity<List<RecoveryCurveProfile>> getAll() {
        return ResponseEntity.ok(service.getAllCurves());
    }
}
