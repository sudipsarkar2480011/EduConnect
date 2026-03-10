package com.educonnect.controller;

import com.educonnect.dto.demographics.DemographicsRequestDTO;
import com.educonnect.model.demographics.StudentDemographics;
import com.educonnect.service.contract.StudentDemographicsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/student/{studentId}/demographics")
@RequiredArgsConstructor
public class StudentDemographicsController {

    private final StudentDemographicsService demographicsService;

    /**
     * POST: Create demographics for a student
     * URL: http://localhost:8080/api/v1/students/{UUID}/demographics
     */
    @PostMapping
    public ResponseEntity<StudentDemographics> createDemographics(
            @PathVariable UUID studentId,
            @Valid @RequestBody DemographicsRequestDTO requestDTO) {
        return new ResponseEntity<>(demographicsService.saveDemographics(studentId, requestDTO), HttpStatus.CREATED);
    }

    /**
     * GET: Fetch demographics for a student
     * URL: http://localhost:8080/api/v1/students/{UUID}/demographics
     */
    @GetMapping
    public ResponseEntity<StudentDemographics> getDemographics(@PathVariable UUID studentId) {
        return ResponseEntity.ok(demographicsService.getDemographics(studentId));
    }

    /**
     * PUT: Update demographics
     * URL: http://localhost:8080/api/v1/students/{UUID}/demographics
     */
    @PutMapping
    public ResponseEntity<StudentDemographics> updateDemographics(
            @PathVariable UUID studentId,
            @Valid @RequestBody DemographicsRequestDTO requestDTO) {
        return ResponseEntity.ok(demographicsService.updateDemographics(studentId, requestDTO));
    }
}