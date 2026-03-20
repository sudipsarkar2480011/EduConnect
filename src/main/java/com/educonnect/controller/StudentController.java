package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.exception.custom_exceptions.InvalidUserException;
import com.educonnect.exception.custom_exceptions.UserIdDoNothMatchException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.service.contract.course.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing individual student resources.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/students")
@Tag(name = "02 StudentController")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    /**
     * Fetches all student records.
     */
    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    /**
     * Retrieves a student profile by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        return ResponseEntity.ok(studentService.getById(studentId));
    }

    /**
     * Updates profile data for the authenticated student.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable("id") UUID studentId,
            @Valid @RequestBody StudentUpdateRequest request,
            @AuthenticationPrincipal UserPrinciples principles
    ) throws UserNotFoundException, InvalidUserException
    {
        if(!principles.getUser().getUserId().equals(studentId)){
            throw new InvalidUserException("Access Denied: Cannot modify other users.");
        }
        return ResponseEntity.ok(studentService.update(studentId, request));
    }

    /**
     * Removes a student from the system.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        studentService.delete(studentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Enrolls a student into a course.
     * @param studentId The ID of the student from the URL.
     * @param courseId A DTO containing the courseId
     */
    @PostMapping("/{id}/enrollments")
    public ResponseEntity<StudentResponse> enrollToCourse(
            @PathVariable("id") UUID studentId,
            @PathVariable("courseId") UUID courseId) throws UserNotFoundException, UserIdDoNothMatchException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.addStudentToCourse(studentId, courseId));
    }
}