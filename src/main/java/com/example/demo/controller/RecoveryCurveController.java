package com.example.demo.controller;

import com.example.demo.model.RecoveryCurveProfile;
import com.example.demo.service.RecoveryCurveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recovery-curves")
@Tag(name = "Recovery Curves")
public class RecoveryCurveController {

    private final RecoveryCurveService service;

    public RecoveryCurveController(RecoveryCurveService service) {
        this.service = service;
    }

    @PostMapping
    public RecoveryCurveProfile create(@RequestBody RecoveryCurveProfile curve) {
        return service.createCurveEntry(curve);
    }

    @GetMapping("/surgery/{type}")
    public List<RecoveryCurveProfile> getBySurgery(@PathVariable String type) {
        return service.getCurveForSurgery(type);
    }

    @GetMapping
    public List<RecoveryCurveProfile> getAll() {
        return service.getAllCurves();
    }
}
