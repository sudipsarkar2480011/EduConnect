package com.educonnect.controller;

import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.implementation.report.ReportServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/student")
@Tag(name = "02 StudentController")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final ReportServiceImpl reportService;

    @GetMapping("/all")
    public ResponseEntity<List<StudentResponse>> getAllStudentsReport() {
        return ResponseEntity.ok(reportService.getAllStudents());
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        return ResponseEntity.ok(studentService.getById(studentId));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @PostMapping("{id}/update")
    public ResponseEntity<StudentResponse> update(
            @PathVariable("id") UUID studentId,
            @Valid @RequestBody StudentUpdateRequest request
    ) throws UserNotFoundException {
        return ResponseEntity.ok(studentService.update(studentId, request));
    }

    @PostMapping("{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        studentService.delete(studentId);
        return new ResponseEntity<>("Deleted Successfully ", HttpStatus.OK);
    }

    @PostMapping("/add-student")
    public ResponseEntity<StudentResponse> studentEnrollToCourse(@RequestParam UUID studentId, @RequestParam UUID courseId) throws UserNotFoundException {
        return new ResponseEntity<>(courseService.addStudentToCourse(studentId,courseId), HttpStatus.OK);
    }

}