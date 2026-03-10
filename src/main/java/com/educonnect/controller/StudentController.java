package com.educonnect.controller;

import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.service.contract.course.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * REST controller for student-related operations.
 * <p>Provides endpoints for managing student profiles, retrieving student data,
 * and handling course enrollments.</p>
 *
 * @author harini
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/student")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    /**
     * Retrieves a specific student's profile by their unique identifier.
     *
     * @param studentId the UUID of the student to find.
     * @return a {@link ResponseEntity} containing the {@link StudentResponse}.
     * @throws UserNotFoundException if no student exists with the provided ID.
     */
    @GetMapping("{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        return ResponseEntity.ok(studentService.getById(studentId));
    }

    /**
     * Retrieves a list of all students registered in the EduConnect system.
     *
     * @return a {@link ResponseEntity} containing a list of {@link StudentResponse} DTOs.
     */
    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    /**
     * Updates an existing student's profile information.
     *
     * @param studentId the UUID of the student to update.
     * @param request the validated update data (e.g., name, email).
     * @return a {@link ResponseEntity} containing the updated {@link StudentResponse}.
     * @throws UserNotFoundException if the target student is not found.
     */
    @PostMapping("{id}/update")
    public ResponseEntity<StudentResponse> update(
            @PathVariable("id") UUID studentId,
            @Valid @RequestBody StudentUpdateRequest request
    ) throws UserNotFoundException {
        return ResponseEntity.ok(studentService.update(studentId, request));
    }

    /**
     * Deletes a student record from the system.
     *
     * @param studentId the UUID of the student to be removed.
     * @return a {@link ResponseEntity} with a success message.
     * @throws UserNotFoundException if the student does not exist.
     */

    @PostMapping("{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") UUID studentId) throws UserNotFoundException {
        studentService.delete(studentId);
        return new ResponseEntity<>("Deleted Successfully ", HttpStatus.OK);
    }

    /**
     * Enrolls a student into a specific course using request parameters.
     * <p>This endpoint communicates with the {@link CourseService} to create an
     * enrollment bridge between the student and the course content.</p>
     *
     * @param studentId the UUID of the student.
     * @param courseId the UUID of the course.
     * @return a {@link ResponseEntity} with the updated enrollment status.
     */
    @PostMapping("/add-student")
    public ResponseEntity<StudentResponse> studentEnrollToCourse(@RequestParam UUID studentId, @RequestParam UUID courseId)
    {
        return new ResponseEntity<>(courseService.addStudentToCourse(studentId,courseId), HttpStatus.OK);
    }

}