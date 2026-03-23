package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.exception.custom_exceptions.InvalidUserException;
import com.educonnect.exception.custom_exceptions.UserIdDoNothMatchException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Student;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        return ResponseEntity.ok(studentService.getById(studentId));
    }

    @PutMapping("{id}/update")
    public ResponseEntity<StudentResponse> update(
            @PathVariable("id") UUID studentId,
            @Valid @RequestBody StudentUpdateRequest request,
            @AuthenticationPrincipal UserPrinciples principles
            ) throws UserNotFoundException, InvalidUserException
    {
        /*
        * Ensures an user is modifying their own data not any other user's
        * */
        Student student=studentService.getByStudentId(studentId);
        if(student.getParent()==null && !principles.getUser().getUserId().equals(studentId)){
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
    public ResponseEntity<StudentResponse> studentEnrollToCourse( @RequestParam UUID courseId , @AuthenticationPrincipal UserPrinciples userPrinciples) throws UserNotFoundException, UserIdDoNothMatchException {

            return new ResponseEntity<>(courseService.addStudentToCourse(courseId, userPrinciples.getUser().getUserId()), HttpStatus.OK);

    }


}