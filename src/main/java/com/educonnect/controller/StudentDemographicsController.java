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

/**
 * REST controller for managing Student Demographics.
 * Provides endpoints for creating, retrieving, and updating detailed student profile information.
 *
 * @author Sankha Subhra Chakraborty
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequestMapping("/api/v1/student/{studentId}/demographics")
@RequiredArgsConstructor
public class StudentDemographicsController {

    private final StudentDemographicsService demographicsService;

    /**
     * Registers new demographic information for a specific student.
     *
     * @param studentId The unique identifier of the student
     * @param requestDTO The demographic data to be saved
     * @return {@link ResponseEntity}<{@link StudentDemographics}> The created demographic record
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity<StudentDemographics> createDemographics(
            @PathVariable UUID studentId,
            @Valid @RequestBody DemographicsRequestDTO requestDTO) {
        return new ResponseEntity<>(demographicsService.saveDemographics(studentId, requestDTO), HttpStatus.CREATED);
    }

    /**
     * Retrieves the demographic details associated with a specific student ID.
     *
     * @param studentId The unique identifier of the student
     * @return {@link ResponseEntity}<{@link StudentDemographics}> The found demographic record
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<StudentDemographics> getDemographics(@PathVariable UUID studentId) {
        return ResponseEntity.ok(demographicsService.getDemographics(studentId));
    }

    /**
     * Updates the existing demographic information for a specific student.
     *
     * @param studentId The unique identifier of the student
     * @param requestDTO The updated demographic data
     * @return {@link ResponseEntity}<{@link StudentDemographics}> The updated demographic record
     * @since 1.0
     */
    @PutMapping
    public ResponseEntity<StudentDemographics> updateDemographics(
            @PathVariable UUID studentId,
            @Valid @RequestBody DemographicsRequestDTO requestDTO) {
        return ResponseEntity.ok(demographicsService.updateDemographics(studentId, requestDTO));
    }
}