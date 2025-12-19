package com.example.demo.controller;

import com.example.demo.model.DailySymptomLog;
import com.example.demo.service.DailySymptomLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symptom-logs")
@Tag(name = "Daily Symptom Logs")
public class DailySymptomLogController {

    private final DailySymptomLogService service;

    public DailySymptomLogController(DailySymptomLogService service) {
        this.service = service;
    }

    @PostMapping
    public DailySymptomLog record(@RequestBody DailySymptomLog log) {
        return service.recordSymptomLog(log);
    }

    @PutMapping("/{id}")
    public DailySymptomLog update(
            @PathVariable Long id,
            @RequestBody DailySymptomLog log) {
        return service.updateSymptomLog(id, log);
    }

    @GetMapping("/patient/{patientId}")
    public List<DailySymptomLog> getByPatient(@PathVariable Long patientId) {
        return service.getLogsByPatient(patientId);
    }
}
