package com.educonnect.controller;

import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentRegisterRequest;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/student")
@Validated
public class StudentController {

    private final StudentService studentService;



    // --- READ ---
    @GetMapping("{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") UUID studentId) {
        return ResponseEntity.ok(studentService.getById(studentId));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.getAll());
    }


    @GetMapping("/by-email")
    public ResponseEntity<StudentResponse> findByEmail(@RequestParam @Email String email) {
        StudentResponse resp = studentService.getByEmail(email);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/by-fullname")
    public ResponseEntity<List<StudentResponse>> findByFullName(@RequestParam String fullName) {
        List<StudentResponse> resp = studentService.findByFullName(fullName);
        return ResponseEntity.ok(resp);
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