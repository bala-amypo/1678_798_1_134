package com.example.demo.controller;

import com.example.demo.model.PatientProfile;
import com.example.demo.service.PatientProfileService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Patients", description = "Patient management endpoints")
public class PatientController {

    private final PatientProfileService patientProfileService;

    @Operation(summary = "Create a new patient", 
               description = "Creates a new patient profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Patient created successfully",
                    content = @Content(schema = @Schema(implementation = PatientProfile.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Patient ID already exists")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<PatientProfile> createPatient(
            @Parameter(description = "Patient profile details", required = true)
            @Valid @RequestBody PatientProfile patient) {
        
        log.info("Creating new patient with ID: {}", patient.getPatientId());
        
        PatientProfile createdPatient = patientProfileService.createPatient(patient);
        
        log.info("Patient created successfully with ID: {}", createdPatient.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @Operation(summary = "Get patient by ID", 
               description = "Returns a single patient by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient found",
                    content = @Content(schema = @Schema(implementation = PatientProfile.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<PatientProfile> getPatientById(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable Long id) {
        
        log.info("Fetching patient with ID: {}", id);
        
        PatientProfile patient = patientProfileService.getPatientById(id);
        
        return ResponseEntity.ok(patient);
    }

    @Operation(summary = "Get patient by patient ID", 
               description = "Returns a single patient by their patient ID (external ID)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient found",
                    content = @Content(schema = @Schema(implementation = PatientProfile.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patient-id/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<PatientProfile> getPatientByPatientId(
            @Parameter(description = "Patient external ID", required = true)
            @PathVariable String patientId) {
        
        log.info("Fetching patient with patient ID: {}", patientId);
        
        return patientProfileService.findByPatientId(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all patients", 
               description = "Returns a list of all patients with pagination and filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patients retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PatientProfile.class))))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<Map<String, Object>> getAllPatients(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(defaultValue = "desc") String direction,
            
            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active,
            
            @Parameter(description = "Filter by surgery type")
            @RequestParam(required = false) String surgeryType,
            
            @Parameter(description = "Filter by surgery date from")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate surgeryDateFrom,
            
            @Parameter(description = "Filter by surgery date to")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate surgeryDateTo) {
        
        log.info("Fetching all patients - page: {}, size: {}", page, size);
        
        Sort sort = direction.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // In a real application, you would have a repository method with filters
        // For now, we'll get all and filter in memory (not efficient for large datasets)
        List<PatientProfile> allPatients = patientProfileService.getAllPatients();
        
        // Apply filters
        List<PatientProfile> filteredPatients = allPatients.stream()
                .filter(patient -> active == null || patient.getActive() == active)
                .filter(patient -> surgeryType == null || 
                        surgeryType.equalsIgnoreCase(patient.getSurgeryType()))
                .filter(patient -> surgeryDateFrom == null || 
                        (patient.getSurgeryDate() != null && 
                         !patient.getSurgeryDate().isBefore(surgeryDateFrom)))
                .filter(patient -> surgeryDateTo == null || 
                        (patient.getSurgeryDate() != null && 
                         !patient.getSurgeryDate().isAfter(surgeryDateTo)))
                .toList();
        
        // Apply pagination manually (for demo purposes)
        int start = Math.min((int) pageable.getOffset(), filteredPatients.size());
        int end = Math.min((start + pageable.getPageSize()), filteredPatients.size());
        List<PatientProfile> pageContent = filteredPatients.subList(start, end);
        
        Map<String, Object> response = new HashMap<>();
        response.put("patients", pageContent);
        response.put("currentPage", page);
        response.put("totalItems", filteredPatients.size());
        response.put("totalPages", (int) Math.ceil((double) filteredPatients.size() / size));
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update patient status", 
               description = "Updates the active status of a patient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient status updated",
                    content = @Content(schema = @Schema(implementation = PatientProfile.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<PatientProfile> updatePatientStatus(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "New active status", required = true)
            @RequestParam Boolean active) {
        
        log.info("Updating status for patient ID {} to active={}", id, active);
        
        PatientProfile updatedPatient = patientProfileService.updatePatientStatus(id, active);
        
        log.info("Patient status updated successfully for ID: {}", id);
        
        return ResponseEntity.ok(updatedPatient);
    }

    @Operation(summary = "Update patient", 
               description = "Updates patient information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient updated",
                    content = @Content(schema = @Schema(implementation = PatientProfile.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN')")
    public ResponseEntity<PatientProfile> updatePatient(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Updated patient details", required = true)
            @Valid @RequestBody PatientProfile patientDetails) {
        
        log.info("Updating patient with ID: {}", id);
        
        // Get existing patient
        PatientProfile existingPatient = patientProfileService.getPatientById(id);
        
        // Update fields (in a real app, use a DTO and mapping)
        existingPatient.setFullName(patientDetails.getFullName());
        existingPatient.setAge(patientDetails.getAge());
        existingPatient.setEmail(patientDetails.getEmail());
        existingPatient.setSurgeryType(patientDetails.getSurgeryType());
        existingPatient.setSurgeryDate(patientDetails.getSurgeryDate());
        
        // Note: patientId should not be changed
        // Note: In a real app, this would be in a service method
        
        log.info("Patient updated successfully for ID: {}", id);
        
        return ResponseEntity.ok(existingPatient);
    }

    @Operation(summary = "Delete patient", 
               description = "Deletes a patient by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable Long id) {
        
        log.info("Deleting patient with ID: {}", id);
        
        patientProfileService.deletePatient(id);
        
        log.info("Patient deleted successfully for ID: {}", id);
        
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get active patients", 
               description = "Returns a list of all active patients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active patients retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PatientProfile.class))))
    })
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<PatientProfile>> getActivePatients() {
        
        log.info("Fetching all active patients");
        
        List<PatientProfile> allPatients = patientProfileService.getAllPatients();
        List<PatientProfile> activePatients = allPatients.stream()
                .filter(patient -> patient.getActive() != null && patient.getActive())
                .toList();
        
        return ResponseEntity.ok(activePatients);
    }

    @Operation(summary = "Get patients by surgery type", 
               description = "Returns patients filtered by surgery type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patients retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PatientProfile.class))))
    })
    @GetMapping("/surgery-type/{surgeryType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<PatientProfile>> getPatientsBySurgeryType(
            @Parameter(description = "Surgery type", required = true)
            @PathVariable String surgeryType) {
        
        log.info("Fetching patients with surgery type: {}", surgeryType);
        
        List<PatientProfile> allPatients = patientProfileService.getAllPatients();
        List<PatientProfile> filteredPatients = allPatients.stream()
                .filter(patient -> surgeryType.equalsIgnoreCase(patient.getSurgeryType()))
                .toList();
        
        return ResponseEntity.ok(filteredPatients);
    }

    @Operation(summary = "Search patients", 
               description = "Search patients by name, email, or patient ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PatientProfile.class))))
    })
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLINICIAN', 'HEALTH_ASSISTANT')")
    public ResponseEntity<List<PatientProfile>> searchPatients(
            @Parameter(description = "Search query")
            @RequestParam String query) {
        
        log.info("Searching patients with query: {}", query);
        
        List<PatientProfile> allPatients = patientProfileService.getAllPatients();
        List<PatientProfile> searchResults = allPatients.stream()
                .filter(patient -> 
                    (patient.getFullName() != null && 
                     patient.getFullName().toLowerCase().contains(query.toLowerCase())) ||
                    (patient.getEmail() != null && 
                     patient.getEmail().toLowerCase().contains(query.toLowerCase())) ||
                    (patient.getPatientId() != null && 
                     patient.getPatientId().toLowerCase().contains(query.toLowerCase())))
                .toList();
        
        return ResponseEntity.ok(searchResults);
    }
}