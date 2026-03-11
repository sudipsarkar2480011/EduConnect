package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.exception.custom_exceptions.InvalidUserException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.service.contract.course.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/student")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        return ResponseEntity.ok(studentService.getById(studentId));
    }

    @PostMapping("{id}/update")
    public ResponseEntity<StudentResponse> update(
            @PathVariable("id") UUID studentId,
            @Valid @RequestBody StudentUpdateRequest request,
            @AuthenticationPrincipal UserPrinciples principles
            ) throws UserNotFoundException, InvalidUserException
    {
        /*
        * Ensures an user is modifying their own data not any other user's
        * */
        if(!principles.getUser().getUserId().equals(studentId)){
                throw new InvalidUserException("Access Denied.");
        }
        return ResponseEntity.ok(studentService.update(studentId, request));
    }

    @PostMapping("{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        studentService.delete(studentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-student")
    public ResponseEntity<StudentResponse> studentEnrollToCourse(@RequestParam UUID studentId, @RequestParam UUID courseId) throws UserNotFoundException {
        return new ResponseEntity<>(courseService.addStudentToCourse(studentId,courseId), HttpStatus.OK);
    }

}