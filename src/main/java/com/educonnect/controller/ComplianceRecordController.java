package com.educonnect.controller;

import com.educonnect.dto.compliance.ComplianceRecordRequestDTO;
import com.educonnect.dto.compliance.ComplianceRecordResponseDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.service.contract.compliance.ComplianceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/compliance")
@RequiredArgsConstructor
public class ComplianceRecordController {

    private final ComplianceRecordService complianceService;
    // POST: Create a new record
    @PostMapping("/create")
    public ResponseEntity<ComplianceRecordResponseDTO> createRecord(@RequestBody ComplianceRecordRequestDTO dto) throws UserNotFoundException {
        return new ResponseEntity<>(complianceService.createRecord(dto), HttpStatus.CREATED);
    }

    // PUT: Update an existing record
    @PutMapping("/update/{id}")
    public ResponseEntity<ComplianceRecordResponseDTO> updateRecord(
            @PathVariable UUID id,
            @RequestBody ComplianceRecordRequestDTO dto) {
        return ResponseEntity.ok(complianceService.updateRecord(id, dto));
    }

    // GET: Retrieve a specific record
    @GetMapping("/get/{id}")
    public ResponseEntity<ComplianceRecordResponseDTO> getRecord(@PathVariable UUID id) {
        return ResponseEntity.ok(complianceService.getRecordById(id));
    }

    // GET: Retrieve all records
    @GetMapping("/list")
    public ResponseEntity<List<ComplianceRecordResponseDTO>> getAllRecords() {
        return ResponseEntity.ok(complianceService.getAllRecords());
    }
}