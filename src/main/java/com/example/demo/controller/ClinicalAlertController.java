    // package com.example.demo.controller;

    // import com.example.demo.model.ClinicalAlertRecord;
    // import com.example.demo.service.ClinicalAlertService;
    // import io.swagger.v3.oas.annotations.tags.Tag;
    // import javax.validation.Valid;
    // import org.springframework.http.ResponseEntity;
    // import org.springframework.web.bind.annotation.*;

    // import java.util.List;
    // import java.util.Optional;

    // @RestController
    // @RequestMapping("/api/alerts")
    // @Tag(name = "Clinical Alerts", description = "Clinical alert management")
    // public class ClinicalAlertController {
    //     private final ClinicalAlertService clinicalAlertService;

    //     public ClinicalAlertController(ClinicalAlertService clinicalAlertService) {
    //         this.clinicalAlertService = clinicalAlertService;
    //     }

    //     @PostMapping
    //     public ResponseEntity<ClinicalAlertRecord> createAlert(@Valid @RequestBody ClinicalAlertRecord alert) {
    //         ClinicalAlertRecord created = clinicalAlertService.createAlert(alert);
    //         return ResponseEntity.ok(created);
    //     }

    //     @PutMapping("/{id}/resolve")
    //     public ResponseEntity<ClinicalAlertRecord> resolveAlert(@PathVariable Long id) {
    //         ClinicalAlertRecord resolved = clinicalAlertService.resolveAlert(id);
    //         return ResponseEntity.ok(resolved);
    //     }

    //     @GetMapping("/patient/{patientId}")
    //     public ResponseEntity<List<ClinicalAlertRecord>> getAlertsForPatient(@PathVariable Long patientId) {
    //         List<ClinicalAlertRecord> alerts = clinicalAlertService.getAlertsByPatient(patientId);
    //         return ResponseEntity.ok(alerts);
    //     }

    //     @GetMapping("/{id}")
    //     public ResponseEntity<ClinicalAlertRecord> getAlertById(@PathVariable Long id) {
    //         Optional<ClinicalAlertRecord> alert = clinicalAlertService.getAlertById(id);
    //         return alert.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    //     }

    //     @GetMapping
    //     public ResponseEntity<List<ClinicalAlertRecord>> getAllAlerts() {
    //         List<ClinicalAlertRecord> alerts = clinicalAlertService.getAllAlerts();
    //         return ResponseEntity.ok(alerts);
    //     }
    // }