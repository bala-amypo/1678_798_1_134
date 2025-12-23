// package com.example.demo.controller;

// import com.example.demo.model.RecoveryCurveProfile;
// import com.example.demo.service.RecoveryCurveService;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import javax.validation.Valid;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/recovery-curves")
// @Tag(name = "Recovery Curves", description = "Recovery curve profile management")
// public class RecoveryCurveController {
//     private final RecoveryCurveService recoveryCurveService;

//     public RecoveryCurveController(RecoveryCurveService recoveryCurveService) {
//         this.recoveryCurveService = recoveryCurveService;
//     }

//     @PostMapping
//     public ResponseEntity<RecoveryCurveProfile> createCurveEntry(@Valid @RequestBody RecoveryCurveProfile entry) {
//         RecoveryCurveProfile created = recoveryCurveService.createCurveEntry(entry);
//         return ResponseEntity.ok(created);
//     }

//     @GetMapping("/surgery/{surgeryType}")
//     public ResponseEntity<List<RecoveryCurveProfile>> getCurveForSurgery(@PathVariable String surgeryType) {
//         List<RecoveryCurveProfile> curves = recoveryCurveService.getCurveForSurgery(surgeryType);
//         return ResponseEntity.ok(curves);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<RecoveryCurveProfile> getCurveById(@PathVariable Long id) {
//         Optional<RecoveryCurveProfile> curve = recoveryCurveService.getCurveById(id);
//         return curve.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//     }

//     @GetMapping
//     public ResponseEntity<List<RecoveryCurveProfile>> getAllCurves() {
//         List<RecoveryCurveProfile> curves = recoveryCurveService.getAllCurves();
//         return ResponseEntity.ok(curves);
//     }
// }