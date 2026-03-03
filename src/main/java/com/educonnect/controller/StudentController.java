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

    @GetMapping("{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") UUID studentId) {
        return ResponseEntity.ok(studentService.getById(studentId));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {

        return ResponseEntity.ok(studentService.getAll());
    }

    @PostMapping("update/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable("id") UUID studentId,
            @Valid @RequestBody StudentUpdateRequest request
    ) {
        return ResponseEntity.ok(studentService.update(studentId, request));
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID studentId) {
        studentService.delete(studentId);
        return ResponseEntity.ok("student deleted : ");
    }
}