package com.educonnect.controller;

import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentRegisterRequest;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // --- READ ---
    @GetMapping("{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") UUID studentId) {
        return ResponseEntity.ok(studentService.getById(studentId));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    // --- CREATE (POST) — registration via strategy (hash password + role=STUDENT) ---
    @PostMapping("register")
    public ResponseEntity<StudentResponse> register(@Valid @RequestBody StudentRegisterRequest request) {
        StudentResponse created = studentService.register(request);
        return ResponseEntity.created(URI.create("/v1/api/student/" + created.userId())).body(created);
    }

    // --- UPDATE (POST) ---
    @PostMapping("{id}/update")
    public ResponseEntity<StudentResponse> update(
            @PathVariable("id") UUID studentId,
            @Valid @RequestBody StudentUpdateRequest request
    ) {
        return ResponseEntity.ok(studentService.update(studentId, request));
    }

    // --- DELETE (POST) ---
    @PostMapping("{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID studentId) {
        studentService.delete(studentId);
        return ResponseEntity.noContent().build();
    }
}